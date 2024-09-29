package com.example.safeT.kickboard.controller;

import com.example.safeT.kickboard.entity.Kickboard;
import com.example.safeT.kickboard.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 킥보드 대여 요청 처리
    @PostMapping
    public ResponseEntity<String> rentKickboard(@RequestParam Long kickboardId,
                                                @RequestParam Long userId,
                                                @RequestParam Long penaltyId) {
        try {
            // 얼굴 인식 기능 호출
            String faceRecognitionResult = aiService.sendUserIdToPython(userId.toString());

            // 얼굴 인식 결과가 일치하지 않으면 대여 불가능
            if (!"동일인입니다.".equals(faceRecognitionResult)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Face recognition did not match. Rental not allowed.");
            }

            // 킥보드 대여 처리
            String result = rentService.rentKickboard(kickboardId, userId, penaltyId);
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