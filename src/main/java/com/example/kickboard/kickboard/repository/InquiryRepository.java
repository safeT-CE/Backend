package com.example.kickboard.kickboard.repository;

import com.example.kickboard.kickboard.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    
    List<Inquiry> findByUserId(Long userId);
}
