// sms 인증 기능 처리를 위한 컨트롤러 클래스

package com.example.kickboard.sms.controller;

import com.example.kickboard.sms.dto.UserRequest;
import com.example.kickboard.sms.dto.base.DefaultRes;
import com.example.kickboard.sms.exception.CustomExceptions;
import com.example.kickboard.sms.exception.ResponseMessage;
import com.example.kickboard.sms.exception.StatusCode;
import com.example.kickboard.sms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sms-certification")
public class SmsController extends BaseController {
    private final UserService userService;

    // 인증 번호 전송
    @PostMapping("/send")
    public ResponseEntity<?> sendSms(@RequestBody UserRequest.SmsCertificationRequest requestDto) throws Exception {
        try {
            userService.sendSms(requestDto);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SMS_CERT_MESSAGE_SUCCESS), HttpStatus.OK); // sms 인증 번호 전송 성공
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    // 인증 번호 확인
    @PostMapping("/confirm")
    public ResponseEntity<Void> SmsVerification(@RequestBody UserRequest.SmsCertificationRequest requestDto) throws Exception{
        try {
            userService.verifySms(requestDto);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SMS_CERT_SUCCESS), HttpStatus.OK); // sms 인증 번호 확인 완료 (본인 인증 성공)
        } catch (CustomExceptions.Exception e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
}