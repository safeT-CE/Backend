package com.example.kickboard.kickboard.controller;

import com.example.kickboard.kickboard.entity.FAQ;
import com.example.kickboard.kickboard.service.FAQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faqs")
public class FAQController {

    @Autowired
    private FAQService faqService;

    // 자주 묻는 질문 목록 조회
    @GetMapping
    public ResponseEntity<List<FAQ>> getAllFAQs() {
        List<FAQ> faqs = faqService.getAllFAQs();
        return ResponseEntity.ok(faqs);
    }

    // 자주 묻는 질문 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<FAQ> getFAQById(@PathVariable Long id) {
        FAQ faq = faqService.getFAQById(id);
        return faq != null ? ResponseEntity.ok(faq) : ResponseEntity.notFound().build();
    }
}
