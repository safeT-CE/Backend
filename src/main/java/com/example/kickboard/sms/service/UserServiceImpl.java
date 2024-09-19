// UserService 인터페이스 구현 클래스. sms 인증 기능 구현

package com.example.kickboard.sms.service;
import com.example.kickboard.sms.dao.SmsDao;
import com.example.kickboard.sms.dto.UserRequest;
import com.example.kickboard.sms.exception.CustomExceptions;
import com.example.kickboard.sms.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SmsUtil smsUtil;
    private final SmsDao smsCertificationDao;

    public void sendSms(UserRequest.SmsCertificationRequest requestDto){ // 인증 코드 생성 및 sms 발송, 생성된 인증 정보 DB 저장
        String to = requestDto.getPhone();
        int randomNumber = (int) (Math.random() * 9000) + 1000; // 인증 코드 난수 생성
        String certificationNumber = String.valueOf(randomNumber);
        smsUtil.sendSms(to, certificationNumber);
        smsCertificationDao.createSmsCertification(to,certificationNumber);
    }

    public void verifySms(UserRequest.SmsCertificationRequest requestDto) { // 사용자가 입력한 인증 번호가 redis에 저장된 인증 번호와 동일한지 확인 -> 인증 실패 시 예외 발생, 인증 성공 시 저장된 인증 정보 삭제
        if (isVerify(requestDto)) {
            throw new CustomExceptions.SmsCertificationNumberMismatchException("인증번호가 일치하지 않습니다.");
        }
        smsCertificationDao.removeSmsCertification(requestDto.getPhone());
    }

    public boolean isVerify(UserRequest.SmsCertificationRequest requestDto) {
        return !(smsCertificationDao.hasKey(requestDto.getPhone()) &&
                smsCertificationDao.getSmsCertification(requestDto.getPhone())
                        .equals(requestDto.getCertificationNumber()));
    }

}
