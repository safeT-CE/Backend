package com.example.kickboard.login.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String jwtToken;
    private Long userId;

    public LoginResponse(String jwtToken, Long userId) {
        this.jwtToken = jwtToken;
        this.userId = userId;
    }
}
