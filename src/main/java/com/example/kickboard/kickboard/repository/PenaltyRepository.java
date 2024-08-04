package com.example.kickboard.kickboard.repository;

import com.example.kickboard.kickboard.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
     List<Penalty> findByUserId(Long userId);
     List<Penalty> findByIdAndUserId(Long penaltyId, Long userId);

     long countByUserId(Long userId);

     void deleteByDateBefore(LocalDateTime date);
}
