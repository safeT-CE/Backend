package com.example.kickboard.kickboard.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class PenaltySummaryResponse {
    private String content;
    private Date date;
    private int count;
    private Map<String, Object> map;

    public PenaltySummaryResponse(String content, Date date, int count, Map<String, Object> map) {
        this.content = content;
        this.date = date;
        this.count = count;
        this.map = map;
    }
}
