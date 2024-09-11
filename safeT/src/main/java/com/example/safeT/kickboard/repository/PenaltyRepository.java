package com.example.safeT.kickboard.repository;

import com.example.safeT.kickboard.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
     List<Penalty> findByUserId(Long userId);
     List<Penalty> findByIdAndUserId(Long penaltyId, Long userId);

     long countByUserId(Long userId);

     void deleteByDateBefore(LocalDateTime date);

     // 특정 날짜 이전의 모든 ID 조회
     @Query("SELECT p.photo FROM Penalty p WHERE p.date < :cutoffTime")
     List<String> findIdPhotoDateBefore(@Param("cutoffTime") LocalDateTime cutoffTime);
}
