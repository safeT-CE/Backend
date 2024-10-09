// 유저 데이터 전송 객체(DTO) 정의 클래스

package com.example.kickboard.sms.dto;

import lombok.Getter;

public class UserRequest {

    @Getter
    public static class SmsCertificationRequest {

        private String phone;
        private String certificationNumber;
    }
}