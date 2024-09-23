// 사용자 관련 서비스 정의 인터페이스. sms 인증 관련 기능 제공 메서드 선언

package com.example.kickboard.sms.service;


import com.example.kickboard.sms.dto.UserRequest;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    void sendSms(UserRequest.SmsCertificationRequest requestDto);

    void verifySms(UserRequest.SmsCertificationRequest requestDto);

    boolean isVerify(UserRequest.SmsCertificationRequest requestDto);
}
