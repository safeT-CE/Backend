package com.example.kickboard.kickboard.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class PenaltySummaryResponse {
    //
    private Long penaltyId;

    private String content;
    private LocalDateTime date;
    private String photo;
    private int totalCount;
    private Map<String, Object> map;

    public PenaltySummaryResponse(Long penaltyId, String content, LocalDateTime date, String photo, int totalCount, Map<String, Object> map) {
        this.penaltyId = penaltyId;
        this.content = content;
        this.date = date;
        this.photo = photo;
        this.totalCount = totalCount;
        this.map = map;
    }
}
