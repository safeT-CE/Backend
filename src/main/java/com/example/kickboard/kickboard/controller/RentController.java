package com.example.kickboard.kickboard.controller;

import com.example.kickboard.kickboard.dto.RentalRequest;
import com.example.kickboard.kickboard.entity.Kickboard;
import com.example.kickboard.kickboard.service.AIService;
import com.example.kickboard.kickboard.service.RentService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/kickboard/rent")
@Slf4j
public class RentController {

    @Autowired
    private RentService rentService;

    @Autowired
    private AIService aiService;


    // 킥보드 상세 정보 조회
    @GetMapping("/details")
    public ResponseEntity<?> getKickboardDetails(@RequestParam(required = false) String qrCode,
                                                 @RequestParam(required = false) Long rentalId) {
        try {
            Kickboard kickboard;
            if (qrCode != null) {
                kickboard = rentService.getKickboardByQrCode(qrCode);
            } else if (rentalId != null) {
                kickboard = rentService.getKickboardByRentalId(rentalId);
            } else {
                return ResponseEntity.badRequest().body("Either qrCode or rentalId must be provided");
            }

            if (kickboard == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(kickboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving kickboard details.");
        }
    }


    // 대여 킥보드 유효성 검사 : 사용함 : 효
    @PostMapping("/validate")
    public ResponseEntity<String> validateQrOrNumber(@RequestBody RentalRequest rentalRequest) {
        // QR 코드와 모델 중 하나만 있으면 검증을 진행
        boolean isValid = false;
        if (rentalRequest.getQrCode() != null) {
            isValid = rentService.validateRentalByQrCode(rentalRequest.getQrCode());
        } else if (rentalRequest.getModel() != null) {
            isValid = rentService.validateRentalByModel(rentalRequest.getModel());
        }

        if (isValid) {
            return ResponseEntity.ok("대여 가능");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("대여 불가");
        }
    }


    // 얼굴 동일성 인증 : 효
    @PostMapping("/identify")
    public ResponseEntity<String> identifyFace(@RequestParam("userId") String userId,
                                               @RequestParam("faceImage") MultipartFile faceImage) {

        Long user_id = Long.parseLong(userId);
        // 사진 관리
        Path temDir;
        try {
            temDir = Files.createTempDirectory("uploads");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("얼굴 인증 실패");
        }

        // 면허증, 얼굴 사진 저장
        try {
            File faceFile = new File(temDir.toFile(), faceImage.getOriginalFilename());
            faceImage.transferTo(faceFile);

            // 얼굴 인식 기능 호출
            ResponseEntity<Map<String, Object>> faceRecognitionResult = aiService.sendToPython(
                    user_id, faceFile.getAbsolutePath());

            Map<String, Object> responseBody = faceRecognitionResult.getBody();
            String result = (String) responseBody.get("result");
            log.info("RentController1 : {}", result);

            // 얼굴 인식 결과가 일치하지 않으면 대여 불가능
            if (!"동일인입니다.".equals(result)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("등록된 얼굴과 동일하지 않습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("얼굴 동일성 판단 성공");
    }

    // 킥보드 대여하기 후, python코드 실행
    @PostMapping
    public ResponseEntity<String> rentKickboard(@RequestParam("kickboardId") Long kickboardId,
                                                @RequestParam("userId") Long userId) {
        log.info("rentKickboard");
        try {
            // 모든 감지 작업을 비동기로 실행
            CompletableFuture<String> helmetDetectionFuture = aiService.detectHelmet(userId.toString());
            CompletableFuture<String> peopleDetectionFuture = aiService.detectPeople(userId.toString());
            CompletableFuture<String> crosswalkDetectionFuture = aiService.detectCrosswalk(userId.toString());

            // 모든 작업의 결과가 완료될 때까지 대기
            CompletableFuture.allOf(helmetDetectionFuture, peopleDetectionFuture, crosswalkDetectionFuture).join();

            // 결과 가져오기
            String helmetDetectionResult = helmetDetectionFuture.get();
            String peopleDetectionResult = peopleDetectionFuture.get();
            String crosswalkDetectionResult = crosswalkDetectionFuture.get();


            // 헬멧 감지 결과 확인
            if (!"With Helmet".equals(helmetDetectionResult)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Helmet detection failed. A helmet is required.");
            }

            // 2인 이상 탑승 감지 결과 확인
            if ("More than two people on board".equals(peopleDetectionResult)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("More than two people detected. This is not allowed.");
            }

            // 횡단보도 감지 결과 확인
            if ("Crosswalk violation detected".equals(crosswalkDetectionResult)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Crosswalk violation detected. Penalty is recorded.");
            }

            return ResponseEntity.ok("Kickboard successfully rented and all checks passed.");

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace(); // 예외 내용을 출력하여 디버깅
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while renting the kickboard.");
        }
    }

    // 프로세스 종료 메서드
    private void terminateProcess(AtomicReference<Process> processReference) {
        Process process = processReference.get();
        if (process != null && process.isAlive()) {
            process.destroy();
            processReference.set(null);
        }
    }
}