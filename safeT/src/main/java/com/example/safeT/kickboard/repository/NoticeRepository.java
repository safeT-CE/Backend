package com.example.safeT.kickboard.repository;

import com.example.safeT.kickboard.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
