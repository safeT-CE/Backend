package com.example.safeT.kickboard.repository;

import com.example.safeT.kickboard.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByUserId(Long userId);
}
