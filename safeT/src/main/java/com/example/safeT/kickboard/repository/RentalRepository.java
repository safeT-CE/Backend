package com.example.safeT.kickboard.repository;

import com.example.safeT.kickboard.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    // 유저의 전체 이용 내역 조회
    List<Rental> findByUser(User user);

    // 유저의 상세 이용 내역 조회
    Optional<Rental> findById(Long id);
}
