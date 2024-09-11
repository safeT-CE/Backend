package com.example.safeT.kickboard.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class PenaltySummaryResponse {
    private String content;
    private LocalDateTime date;
    private int totalCount;
    private Map<String, Object> map;

    public PenaltySummaryResponse(String content, LocalDateTime date, int totalCount, Map<String, Object> map) {
        this.content = content;
        this.date = date;
        this.totalCount = totalCount;
        this.map = map;
    }
}
