package com.example.kickboard.kickboard.dto;

import lombok.Getter;

// User의 identity에 'AWS S3에 저장된 CSV의 경로'를 등록하기 위한 DTO
@Getter
public class FaceRequest {
    private String userId;
    private String identity;
    private String samePerson;
}
