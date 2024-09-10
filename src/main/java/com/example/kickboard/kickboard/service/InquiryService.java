package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.entity.Inquiry;
import com.example.kickboard.kickboard.repository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    public List<Inquiry> getInquiriesByUserId(Long userId) {
        return inquiryRepository.findByUserId(userId);
    }

    public Inquiry createInquiry(Inquiry inquiry) {
        inquiry.setCreatedAt(LocalDateTime.now());
        return inquiryRepository.save(inquiry);
    }

    public Inquiry getInquiryById(Long id) {
        return inquiryRepository.findById(id).orElse(null);
    }

    public Inquiry updateInquiry(Long id, Inquiry updatedInquiry) {
        if (!inquiryRepository.existsById(id)) {
            return null;
        }
        updatedInquiry.setId(id);
        updatedInquiry.setUpdatedAt(LocalDateTime.now());
        return inquiryRepository.save(updatedInquiry);
    }
}
