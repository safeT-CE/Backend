package com.example.safeT.kickboard.service;

import com.example.safeT.kickboard.entity.Inquiry;
import com.example.safeT.kickboard.repository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    // 사용자 ID로 문의 목록 조회
    public List<Inquiry> getInquiriesByUserId(Long userId) {
        return inquiryRepository.findByUserId(userId);
    }

    // 문의 작성
    public Inquiry createInquiry(Inquiry inquiry) {
        inquiry.setCreatedAt(LocalDateTime.now());
        return inquiryRepository.save(inquiry);
    }

    // ID로 문의 조회
    public Inquiry getInquiryById(Long id) {
        return inquiryRepository.findById(id).orElse(null);
    }

    // 문의 수정
    public Inquiry updateInquiry(Long id, Inquiry updatedInquiry) {
        if (!inquiryRepository.existsById(id)) {
            return null; // 문의가 존재하지 않을 시 null 반환
        }
        updatedInquiry.setId(id);
        updatedInquiry.setUpdatedAt(LocalDateTime.now());
        return inquiryRepository.save(updatedInquiry);
    }

    // 관리자 문의 답변 추가
    public Inquiry respondToInquiry(Long id, String response) {
        Optional<Inquiry> inquiryOpt = inquiryRepository.findById(id);
        if (inquiryOpt.isPresent()) {
            Inquiry inquiry = inquiryOpt.get();
            inquiry.setResponse(response);
            inquiry.setRespondedAt(LocalDateTime.now());
            return inquiryRepository.save(inquiry);
        }
        return null; // 문의가 존재하지 않을 시 null 반환
    }
}
