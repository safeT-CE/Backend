package com.example.safeT.kickboard.controller;

import com.example.safeT.kickboard.entity.Kickboard;
import com.example.safeT.kickboard.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rent")
public class RentController {

    @Autowired
    private RentService rentService;

    // QR 코드 또는 대여 번호를 통해 킥보드 상세 정보 조회
    @GetMapping("/details")
    public ResponseEntity<?> getKickboardDetails(@RequestParam(required = false) String qrCode,
                                                 @RequestParam(required = false) Long rentalId) {
        try {
            Kickboard kickboard;
            if (qrCode != null) {
                // QR 코드로 킥보드 조회
                kickboard = rentService.getKickboardByQrCode(qrCode);
            } else if (rentalId != null) {
                // 대여 ID로 킥보드 조회
                kickboard = rentService.getKickboardByRentalId(rentalId);
            } else {
                // qrCode와 rentalId가 모두 null인 경우
                return ResponseEntity.badRequest().body("Either qrCode or rentalId must be provided");
            }

            if (kickboard == null) {
                // 킥보드를 찾을 수 없는 경우
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(kickboard);
        } catch (Exception e) {
            // 예외 발생 시 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving kickboard details.");
        }
    }

    // 킥보드 대여
    @PostMapping("/rent")
    public ResponseEntity<String> rentKickboard(@RequestParam Long kickboardId,
                                                @RequestParam Long userId,
                                                @RequestParam Long penaltyId) {
        try {
            String result = rentService.rentKickboard(kickboardId, userId, penaltyId);
            if ("Kickboard rented successfully".equals(result)) {
                return ResponseEntity.ok("Kickboard successfully rented.");
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            // 예외 발생 시 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while renting the kickboard.");
        }
    }

    // 킥보드 반납
    @PostMapping("/return")
    public ResponseEntity<String> returnKickboard(@RequestParam Long kickboardId) {
        try {
            String result = rentService.returnKickboard(kickboardId);
            if ("Kickboard returned successfully".equals(result)) {
                return ResponseEntity.ok("Kickboard successfully returned.");
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            // 예외 발생 시 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while returning the kickboard.");
        }
    }
}