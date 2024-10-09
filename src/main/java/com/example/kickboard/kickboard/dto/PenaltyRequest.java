package com.example.kickboard.kickboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class PenaltyRequest {

    private String userId;
    private String content;
    private String photo;
    private String date;    // string으로 받음
    private String location;
    private Map<String, Object> map;
    private int detectionCount;
}

