package com.example.kickboard.web.dto;

import com.example.kickboard.kickboard.entity.PMap;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class PenaltyAllResponse {

    private LocalDateTime date;

    private String content;

    private int penaltyCount;

    private String location;

    private int detectionCount;

    public PenaltyAllResponse(String content,
                                 LocalDateTime date,
                                 int penaltyCount,
                                 String location,
                                 int detectionCount) {
        this.content = content;
        this.date = date;
        this.penaltyCount = penaltyCount;
        this.location = location;
        this.detectionCount = detectionCount;
    }
}
