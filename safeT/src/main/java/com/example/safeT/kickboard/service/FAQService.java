package com.example.safeT.kickboard.service;

import com.example.safeT.kickboard.entity.FAQ;
import com.example.safeT.kickboard.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FAQService {

    @Autowired
    private FAQRepository faqRepository;

    public List<FAQ> getAllFAQs() {
        return faqRepository.findAll();
    }

    public FAQ getFAQById(Long id) {
        return faqRepository.findById(id).orElse(null);
    }
}