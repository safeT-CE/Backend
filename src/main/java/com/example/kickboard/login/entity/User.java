package com.example.kickboard.login.entity;

import com.example.kickboard.kickboard.entity.Rental;
import com.example.kickboard.login.validation.NoHyphen;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalTime;
import java.util.List;

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
    @NoHyphen
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

    @Column(name = "useTime", nullable = false, columnDefinition = "time default '00:00'")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime useTime;

    // identity : csv 저장 위치가 기록됨.
    @Column(name = "identity", columnDefinition = "VARCHAR(255)")
    private String identity;

    @Column(name = "samePerson")
    private String samePerson;


    // Rental과의 관계 설정 (1:N)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Rental> rentals;

    public void setUseTime(LocalTime useTime) {
        this.useTime = useTime.withSecond(0).withNano(0); // 시와 분만 남기고 초와 나노초는 제거
    }
}
