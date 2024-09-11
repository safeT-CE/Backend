package com.example.safeT.kickboard.repository;

import com.example.safeT.kickboard.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 킥보드 ID로 대여 기록 조회
public interface RentalRepository extends JpaRepository<Rental, Long> {
    Optional<Rental> findByKickboardId(Long kickboardId);
    Optional<Rental> findTopByKickboardIdAndReturnedFalseOrderByRentedAtDesc(Long kickboardId);
}
