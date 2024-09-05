package com.example.kickboard.kickboard.dto;

import com.example.kickboard.login.entity.Grade;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
public class ProfileResponse {

    private String phone;
    private Grade grade;
    private int point;
    private LocalTime useTime;

    public ProfileResponse(String phone, Grade grade, int point, LocalTime useTime) {
        this.phone = phone;
        this.grade = grade;
        this.point = point;
        this.useTime = useTime;
    }
}
