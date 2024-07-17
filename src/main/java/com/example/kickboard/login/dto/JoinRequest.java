package com.example.kickboard.login.dto;

import com.example.kickboard.login.entity.Grade;
import com.example.kickboard.login.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String phone;

    @NotBlank(message = "면허증을 등록해주세요.")
    private String identity;

    @NotBlank(message = "얼굴을 인증해주세요.")
    private String face;

    // 비밀번호가 없기 때문에 암호화 X
    public User toEntity() {
        return User.builder()
                .phone(this.phone)
                .identity(this.identity)
                .face(this.face)
                .grade(Grade.Bronze)
                .build();
            }
}
