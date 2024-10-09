package com.example.kickboard.kickboard.repository;

import com.example.kickboard.kickboard.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
