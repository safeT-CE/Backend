package com.example.kickboard.login.dto;

import com.example.kickboard.login.entity.Grade;
import com.example.kickboard.login.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String phone;

    // 비밀번호가 없기 때문에 암호화 X
    public User toEntity() {
        User user = User.builder()
                .phone(this.phone)
                .grade(Grade.Bronze)
                .identity("")
                .build();
        return user;
            }
}
