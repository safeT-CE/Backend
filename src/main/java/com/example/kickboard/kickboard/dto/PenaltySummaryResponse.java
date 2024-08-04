package com.example.kickboard.kickboard.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class PenaltySummaryResponse {
    private String content;
    private String date;
    private int totalCount;
    private Map<String, Object> map;

    public PenaltySummaryResponse(String content, String date, int totalCount, Map<String, Object> map) {
        this.content = content;
        this.date = date;
        this.totalCount = totalCount;
        this.map = map;
    }
}
