package com.example.kickboard.kickboard.controller;


import com.example.kickboard.kickboard.dto.RentalRequest;
import com.example.kickboard.kickboard.entity.Kickboard;
import com.example.kickboard.kickboard.service.AIService;
import com.example.kickboard.kickboard.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/kickboard/rent")
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

        // 사진 관리
        Path temDir;
        try {
            temDir = Files.createTempDirectory("uploads");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("얼굴 인증 실패");
        }

        // 면허증, 얼굴 사진 저장
        File faceFile;
        try {
            faceFile = new File(temDir.toFile(), faceImage.getOriginalFilename());
            faceImage.transferTo(faceFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Flask 서버로 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:5000/face-detection"; // Flask 서버의 엔드포인트 URL
        Map<String, String> request = new HashMap<>();
        request.put("userId", userId);
        request.put("faceFile", faceFile.getAbsolutePath());

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // 얼굴 인식 결과 처리
        if (response.getBody().contains("동일인입니다.")) {
            return ResponseEntity.ok("얼굴 동일성 판단 성공");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("얼굴 동일성 판단 실패");
        }

//        // 면허증, 얼굴 사진 저장
//        try {
//            File faceFile = new File(temDir.toFile(), faceImage.getOriginalFilename());
//            faceImage.transferTo(faceFile);
//
//            // 얼굴 인식 기능 호출
//            String faceRecognitionResult = aiService.sendToPython(
//                    userId.toString(), faceFile.getAbsolutePath());
//
//            // 얼굴 인식 결과가 일치하지 않으면 대여 불가능
//            if (!"동일인입니다.".equals(faceRecognitionResult)) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("얼굴 동일성 판단 실패");
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok("얼굴 동일성 판단 성공");
    }

    // 킥보드 대여 요청 처리
    @PostMapping
    public ResponseEntity<String> rentKickboard(@RequestParam Long kickboardId,
                                                @RequestParam Long userId){
                                                //@RequestParam Long penaltyId) {
        try {
            // 얼굴 인식 기능 호출
            //String faceRecognitionResult = aiService.sendUserIdToPython(userId.toString());

            // 얼굴 인식 결과가 일치하지 않으면 대여 불가능
//            if (!"동일인입니다.".equals(faceRecognitionResult)) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Face recognition did not match. Rental not allowed.");
//            }

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