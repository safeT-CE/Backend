package com.example.safeT.kickboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class PenaltyDetailResponse {
    private String content;
    private LocalDateTime date;
    private int totalCount;

    private String location;
    private Map<String, Object> map;

    private String photo;
    private int detectionCount;

    // 추후 이용 시간 추가

    public PenaltyDetailResponse(String content,
                                 LocalDateTime date,
                                 int totalCount,
                                 String location,
                                 Map<String, Object> map,
                                 String photo,
                                 int detectionCount) {
        this.content = content;
        this.date = date;
        this.totalCount = totalCount;
        this.location = location;
        this.map = map;
        this.photo = photo;
        this.detectionCount = detectionCount;
    }
}