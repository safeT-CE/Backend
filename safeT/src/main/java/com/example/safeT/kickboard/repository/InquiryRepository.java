package com.example.safeT.kickboard.repository;

import com.example.safeT.kickboard.entity.Inquiry;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByUserId(Long userId, Sort sort);

    @Transactional
    @Modifying
    @Query("UPDATE Inquiry i SET i.response = :response WHERE i.id = :id")
    void saveResponse(@Param("id") Long id,@Param("response") String response);

    @Query("SELECT i.response FROM Inquiry i WHERE i.id = :id")
    String findResponseById(@Param("id") Long id);
}
