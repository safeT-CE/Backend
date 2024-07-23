package com.example.kickboard.kickboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class PenaltyDetailResponse {
    private String content;
    private Date date;
    private int count;

    private String location;
    private Map<String, Object> map;

    private String photo;

    // 추후 이용 시간 추가


    public PenaltyDetailResponse(String content,
                                 Date date,
                                 int count,
                                 String location,
                                 Map<String, Object> map,
                                 String photo) {
        this.content = content;
        this.date = date;
        this.count = count;
        this.location = location;
        this.map = map;
        this.photo = photo;
    }
}
