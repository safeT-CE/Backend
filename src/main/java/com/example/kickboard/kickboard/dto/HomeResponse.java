package com.example.kickboard.kickboard.dto;

import com.example.kickboard.login.entity.Grade;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
public class HomeResponse {
    private String phone;
    private Grade grade;
    private LocalTime useTime;

    public HomeResponse(String phone, Grade grade, LocalTime useTime) {
        this.phone = phone;
        this.grade = grade;
        this.useTime = useTime;
    }
}
