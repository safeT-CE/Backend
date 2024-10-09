package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.entity.Notice;
import com.example.kickboard.kickboard.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    // 공지사항 목록 조회
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    // 공지사항 상세 조회
    public Optional<Notice> getNoticeById(Long id) {
        return noticeRepository.findById(id);
    }

    // 공지사항 생성
    public Notice createNotice(Notice notice) {
        notice.setCreatedAt(LocalDateTime.now());
        notice.setUpdatedAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    // 공지사항 수정
    public Notice updateNotice(Long id, Notice notice) {
        if (!noticeRepository.existsById(id)) {
            return null;
        }
        notice.setId(id);
        notice.setUpdatedAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    // 공지사항 삭제
    public boolean deleteNotice(Long id) {
        if (!noticeRepository.existsById(id)) {
            return false;
        }
        noticeRepository.deleteById(id);
        return true;
    }
}
