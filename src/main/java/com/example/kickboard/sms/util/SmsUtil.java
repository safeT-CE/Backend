// sms 인증을 위한 유틸리티 클래스. sms 발송 및 관리 기능

package com.example.kickboard.sms.util;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsUtil {

    //@Value("${coolsms.api.sendNumber}")
    //@Value("01071575997")
    @Value("${COOL_SMS_NUMBER}")
    private String sendNumber;

    //@Value("${coolsms.api.key}")
    @Value("${COOL_SMS_KEY}")
    private String apiKey;

    //@Value("${coolsms.api.secret}")
    //@Value("4YKBPYQCSGYGKXOEQFMAGC3UBSILQ8GB")
    @Value("${COOL_SMS_SECRET}")
    private String apiSecret;

    DefaultMessageService messageService;


    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    // sms 인증 코드 전송
    public SingleMessageSentResponse sendSms(String to, String verificationCode){
        Message message = new Message();
        message.setFrom(sendNumber);
        message.setTo(to);
        message.setText("인증 코드는 " + verificationCode + "입니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);
        return response;
    }
}