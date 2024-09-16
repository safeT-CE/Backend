package com.example.kickboard.kickboard.entity;

import com.example.kickboard.login.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

// 개인 질문 답변


@Getter
@Setter
@Entity
@Table(name = "inquiries")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 2000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime respondedAt;

    @Column(nullable = true)
    private String response;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

}

