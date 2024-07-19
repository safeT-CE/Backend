package com.example.kickboard.login.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalTime;

@Entity
@Builder
@Getter
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable= false)
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "phone", nullable = false, unique= true)
    private String phone;

    @Column(name = "grade", nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade = Grade.Bronze; // 기본값 설정


    @Column(name = "rent", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean rent;

    @Column(name = "point", nullable = false, columnDefinition = "INT DEFAULT 0") // 범위 : 0~10
    private int point;

    @Column(name = "ticket", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean ticket;

    @Column(name = "useTime", nullable = false, columnDefinition = "time default '00:00:00'")
    private LocalTime useTime;

    @Column(name = "face", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'b'")
    private String face;

    // identity : csv 저장 위치가 기록됨.
    @Column(name = "identity", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'x'")
    private String identity;

}
