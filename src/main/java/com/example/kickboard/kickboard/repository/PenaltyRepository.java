package com.example.kickboard.kickboard.repository;

import com.example.kickboard.kickboard.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
     List<Penalty> findByUserId(Long userId);
     long countByUserId(Long userId);
}
