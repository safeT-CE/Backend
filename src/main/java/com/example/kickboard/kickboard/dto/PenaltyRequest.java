package com.example.kickboard.kickboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PenaltyRequest {
    private String content;
    private String photo;
    private String location;
    private Map<String, Object> map;
}

