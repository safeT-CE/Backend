package com.example.kickboard.kickboard.entity;

import com.example.kickboard.login.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
public class Penalty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "penaltyId", updatable= false)
    private Long id;

    @Column(name = "date", updatable= false)
    private LocalDateTime date;

    @Column(name = "content", updatable= false)
    private String content;

    // 이건 삭제 가능성
    @Column(name = "count", updatable= false)
    private int totalCount;

    // 증거 사진 : url
    @Column(name = "photo", updatable= false)
    private String photo;

    // 지번 주소
    @Column(name = "location", updatable= false)
    private String location;

    // 위도, 경도
    @Embedded
    private PMap map;


    // 삭제 가능 : 한 번 대여했을 때 감지되는 횟수(같은 content)
    @Column(name = "detectionCount", updatable = false)
    private int detectionCount;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
