package com.example.kickboard.login.dto;

import com.example.kickboard.kickboard.entity.DataRecord;
import com.example.kickboard.login.entity.Grade;
import com.example.kickboard.login.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String phone;

    @NotEmpty(message = "면허증과 실제 얼굴이 동일하지 않습니다.")
    private List<DataRecord> dataRecords;

    @NotBlank(message = "얼굴을 인증해주세요.")
    private String face;

    // 비밀번호가 없기 때문에 암호화 X
    public User toEntity() {
        User user = User.builder()
                .phone(this.phone)
                .face(this.face)
                .grade(Grade.Bronze)
                .build();
        user.setDataRecords(this.dataRecords);
        return user;
            }
}
