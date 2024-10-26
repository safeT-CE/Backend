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


    // 얼굴 동일성 인증 : 사용함 요청할 때 필요한 코드가 해당 user의 identity랑 인식한 얼굴 사진 : 효
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
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("얼굴 동일성 판단 실패");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("얼굴 동일성 판단 성공");
    }


    // 얼굴 동일성 인증 : userId만 전송했을 때 (확인용)
    @PostMapping("/identify_userId")
    public ResponseEntity<String> identifyFaceUserId(@RequestParam("userId") String userId){

        Long user_id = Long.parseLong(userId);
        // 사진 관리
        Path temDir;
        try {
            temDir = Files.createTempDirectory("uploads");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("얼굴 인증 실패");
        }

        // 얼굴 인식 기능 호출
        ResponseEntity<Map<String, Object>> faceRecognitionResult = aiService.sendToPythonUserId(user_id);
        Map<String, Object> responseBody = faceRecognitionResult.getBody();

        String result = (String) responseBody.get("result");

        // 얼굴 인식 결과가 일치하지 않으면 대여 불가능
        if (!"동일인입니다.".equals(result)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("얼굴 동일성 판단 실패");
        }
        return ResponseEntity.ok("얼굴 동일성 판단 성공");
    }

    // 얼굴 동일성 인증 : faceImage 확인용
    @PostMapping("/reidentify")
    public ResponseEntity<String> ReidentifyFace(@RequestParam("userId") String userId,
                                                 @NotNull @RequestParam("faceImage") MultipartFile faceImage) {

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
            ProcessBuilder pb = new ProcessBuilder("python", "C:/Users/SAMSUNG/Desktop/detection/Detection/redataNface.py",
                    faceFile.getAbsolutePath(),
                    String.valueOf(user_id));
            pb.redirectErrorStream(true);

            Process process = pb.start();

            int exitCode =  process.waitFor();

            // 얼굴 사진 삭제
            Files.delete(faceFile.toPath());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 킥보드 대여 요청 처리
    @PostMapping
    public ResponseEntity<String> rentKickboard(@RequestParam Long kickboardId,
                                                @RequestParam Long userId){
                                                //@RequestParam Long penaltyId) {
        try {

            // 킥보드 대여 처리
            //String result = rentService.rentKickboard(kickboardId, userId, penaltyId);
            String result = rentService.rentKickboard(kickboardId, userId);
            if (!"Kickboard rented successfully".equals(result)) {
                return ResponseEntity.badRequest().body(result);
            }

            // 대여 처리가 완료된 후 헬멧 감지
            String helmetDetectionResult = aiService.detectHelmet(userId.toString());
            if (!"With Helmet".equals(helmetDetectionResult)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Helmet detection failed. A helmet is required.");
            }

            // 2인 이상 탑승 감지
            String peopleDetectionResult = aiService.detectPeople(userId.toString());
            if ("More than two people on board".equals(peopleDetectionResult)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("More than two people detected. This is not allowed.");
            }

            // 횡단보도 인식
            String crosswalkDetectionResult = aiService.detectCrosswalk(userId.toString());
            if ("Crosswalk violation detected".equals(crosswalkDetectionResult)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Crosswalk violation detected. Penalty is recorded.");
            }

            return ResponseEntity.ok("Kickboard successfully rented and all checks passed.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while renting the kickboard.");
        }
    }
}