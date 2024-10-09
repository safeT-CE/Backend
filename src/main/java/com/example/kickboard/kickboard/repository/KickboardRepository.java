package com.example.kickboard.kickboard.repository;

import com.example.kickboard.kickboard.entity.Kickboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KickboardRepository extends JpaRepository<Kickboard, Long> {
    // QR 코드로 킥보드 상세 정보 조회
    Kickboard findByQrCode(String qrCode);

    // 대여 번호로 킥보드 상세 정보 조회
    @Query("SELECT k FROM Kickboard k WHERE k.id = :id")
    Optional<Kickboard> findById(@Param("id") Long id);

    // 킥보드 ID로 킥보드 대여 여부 조회
    @Query("SELECT k.rented FROM Kickboard k WHERE k.id = :id")
    Boolean findRentedById(@Param("id") Long id);

    // 킥보드 이름으로 킥보드 유무 조회
    @Query("SELECT k.rented FROM Kickboard k WHERE k.model = :model")
    Boolean findRentedByModel(@Param("model") Long model);
    @Query("SELECT k.rented FROM Kickboard k WHERE k.qrCode = :qrCode")
    Boolean findRentedByQrCode(@Param("qrCode") String qrCode);

    Kickboard findByModel(Long model);
}
