package com.example.kickboard.kickboard.dto;

import com.example.kickboard.login.entity.Grade;
import com.example.kickboard.login.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ProfileResponse {

    private String phone;
    private Grade grade;
    private int point;
    private String useTime;

    public ProfileResponse(String phone, Grade grade, int point, String useTime) {
        this.phone = phone;
        this.grade = grade;
        this.point = point;
        this.useTime = useTime;
    }
}
