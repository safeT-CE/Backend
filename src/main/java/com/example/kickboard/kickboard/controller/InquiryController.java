package com.example.kickboard.kickboard.controller;

import com.example.kickboard.kickboard.dto.InquiryRequest;
import com.example.kickboard.kickboard.entity.Inquiry;
import com.example.kickboard.kickboard.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inquiries")
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    // 사용자 ID로 개인 문의 사항 조회
    @GetMapping("")
    public ResponseEntity<List<Inquiry>> getInquiriesByUserId(@RequestParam("userId") Long userId) {
        List<Inquiry> inquiries = inquiryService.getInquiriesByUserId(userId);
        return ResponseEntity.ok(inquiries);
    }

    // 개인 문의 글쓰기
    @PostMapping("")
    public ResponseEntity<Inquiry> createInquiry(@RequestParam("userId") Long userId, @RequestBody Inquiry inquiry) {
        Inquiry createdInquiry = inquiryService.createInquiry(userId, inquiry);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInquiry);
    }

    // 개인 문의 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Inquiry> getInquiryById(@PathVariable("id") Long id) {
        Inquiry inquiry = inquiryService.getInquiryById(id);
        return inquiry != null ? ResponseEntity.ok(inquiry) : ResponseEntity.notFound().build();
    }

    // 개인 문의 수정
    @PutMapping("/{id}")
    public ResponseEntity<Inquiry> updateInquiry(@PathVariable Long id, @RequestBody Inquiry updatedInquiry) {
        Inquiry inquiry = inquiryService.updateInquiry(id, updatedInquiry);
        return inquiry != null ? ResponseEntity.ok(inquiry) : ResponseEntity.notFound().build();
    }

    // 관리자 문의 답변 추가
    @PostMapping("/{id}/response")
    public ResponseEntity<Map<String, Object>> respondToInquiry(@PathVariable("id") Long id, @RequestBody InquiryRequest request) {
        boolean answer = inquiryService.respondToInquiry(id, request);
        if (answer) {
            Map<String, Object> response = new HashMap<>();
            response.put("userId", request.getUserId());
            response.put("inquiryId", id);
            response.put("response", request.getResponse());
            return ResponseEntity.ok(response);
        } else
            return ResponseEntity.status(500).body(Map.of("error", "Failed to register response"));
    }
}
