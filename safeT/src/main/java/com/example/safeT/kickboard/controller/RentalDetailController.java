package com.example.safeT.kickboard.controller;

import com.example.safeT.kickboard.dto.RentalDetailDTO;
import com.example.safeT.kickboard.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rent/details")
public class RentalDetailController {

    @Autowired
    private RentalDetailService rentalDetailService;

    // 유저의 전체 이용 내역 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalDetailDTO>> getUserRentalDetails(@PathVariable Long userId) {
        List<RentalDetailDTO> rentalDetails = rentalDetailService.getUserRentalDetails(userId);
        return ResponseEntity.ok(rentalDetails);
    }

    // 상세 이용 내역 조회
    @GetMapping("/{id}")
    public ResponseEntity<RentalDetailDTO> getRentalDetail(@PathVariable Long id) {
        RentalDetailDTO rentalDetail = rentalDetailService.getRentalDetail(id);
        return ResponseEntity.ok(rentalDetail);
    }
}
