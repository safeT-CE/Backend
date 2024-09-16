package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.entity.Inquiry;
import com.example.kickboard.kickboard.repository.InquiryRepository;
import com.example.kickboard.login.entity.User;
import com.example.kickboard.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private UserRepository userRepository;

    // 사용자 ID로 문의 목록 조회
    public List<Inquiry> getInquiriesByUserId(Long userId) {
        return inquiryRepository.findByUserId(userId);
    }

    // 문의 작성
    public Inquiry createInquiry(Long userId, Inquiry inquiry) {
        Optional<User> userOptional = userRepository.findById(userId);

        // 유효한 User
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            inquiry.setUser(user);
            inquiry.setCreatedAt(LocalDateTime.now());
            return inquiryRepository.save(inquiry);
        } else {
            // User가 존재하지 않을 때
            throw new IllegalArgumentException("Invalid userId: " + userId);
        }
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
        //updatedInquiry.setUpdatedAt(LocalDateTime.now());
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
