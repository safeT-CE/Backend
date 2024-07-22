package com.example.kickboard.kickboard.entity;

import com.example.kickboard.login.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "penaltyId", updatable= false)
    private Long id;

    @Column(name = "date", updatable= false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "content", updatable= false)
    private String content;

    // 이건 삭제 가능성
    @Column(name = "count", updatable= false)
    private int count;

    // 증거 사진 : url
    @Column(name = "photo", updatable= false)
    private String photo;

    // 지번 주소
    @Column(name = "location", updatable= false)
    private String location;

    // 위도, 경도
    @Embedded
    private PMap map;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    @PrePersist
    protected void onCreate() {
        date = new Date();
    }
}
