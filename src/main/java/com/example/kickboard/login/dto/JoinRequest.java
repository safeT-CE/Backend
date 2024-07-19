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

    @NotBlank(message = "얼굴을 인증해주세요.")
    private String face;

    @NotBlank(message = "면허증 얼굴과 얼굴이 다릅니다.")
    private String identity;

    // 비밀번호가 없기 때문에 암호화 X
    public User toEntity() {
        User user = User.builder()
                .phone(this.phone)
                .face(this.face)
                .grade(Grade.Bronze)
                .identity(this.identity)
                .build();
        return user;
            }
}
