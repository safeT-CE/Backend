package com.example.kickboard.kickboard.repository;

import com.example.kickboard.kickboard.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 킥보드 ID로 대여 기록 조회 -> 대여 기록 관리
public interface RentalRecordRepository extends JpaRepository<Rental, Long> {
    Optional<Rental> findByKickboardId(Long kickboardId);
    Optional<Rental> findTopByKickboardIdAndReturnedFalseOrderByRentedAtDesc(Long kickboardId);
}