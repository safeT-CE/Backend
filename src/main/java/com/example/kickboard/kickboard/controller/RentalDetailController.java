package com.example.kickboard.kickboard.controller;

import com.example.kickboard.kickboard.dto.RentalDetailResponse;
import com.example.kickboard.kickboard.service.RentalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rent/details", produces = "application/json; charset=UTF-8")
public class RentalDetailController {

    @Autowired
    private RentalDetailService rentalDetailService;

    // 유저의 전체 이용 내역 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalDetailResponse>> getUserRentalDetails(@PathVariable("userId") Long userId) {
        List<RentalDetailResponse> rentalDetails = rentalDetailService.getUserRentalDetails(userId);
        return ResponseEntity.ok(rentalDetails);
    }

    // 상세 이용 내역 조회
    @GetMapping("/{id}")
    public ResponseEntity<RentalDetailResponse> getRentalDetail(@PathVariable("id") Long id) {
        RentalDetailResponse rentalDetail = rentalDetailService.getRentalDetail(id);
        return ResponseEntity.ok(rentalDetail);
    }
}
