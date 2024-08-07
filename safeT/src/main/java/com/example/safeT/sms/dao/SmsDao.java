// sms 인증 번호 저장 관리 DAO(Data Access Object) 클래스. sms 인증 번호 생성, 조회, 삭제 기능

package com.example.safeT.sms.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class SmsDao {

    private final String PREFIX = "sms:"; // redis에 저장될 key 값 (핸드폰 번호) 중복 방지를 위한 상수 선언
    private final int LIMIT_TIME = 3 * 60; // sms 인증 번호 데이터 유효 시간 설정 -> 180초 (3분)
    private final StringRedisTemplate redisTemplate;

    public void createSmsCertification(String phone, String certificationNumber) { // 사용자가 입력한 휴대폰 번호와 인증번호 저장
        redisTemplate.opsForValue()
                .set(PREFIX + phone, certificationNumber, Duration.ofSeconds(LIMIT_TIME)); // 핸드폰 번호(key), 인증번호(value), 유효 시간(duration) 설정
    }

    public String getSmsCertification(String phone) { // sms 인증 번호를 redis에서 조회
        return redisTemplate.opsForValue().get(PREFIX + phone);
    }

    public void removeSmsCertification(String phone) { // 인증 완료 시 메모리 관리를 위해 redis에 저장된 인증번호 삭제
        redisTemplate.delete(PREFIX + phone);
    }

    public boolean hasKey(String phone) { // redis에 해당 핸드폰 번호(key)로 저장된 인증번호(value)가 존재하는지 확인
        return redisTemplate.hasKey(PREFIX + phone);
    }
}
