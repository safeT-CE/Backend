package com.example.safeT.login.service;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendMessageService {

    final DefaultMessageService messageService;
    private static final String DOMAIN = "https://api.coolsms.co.kr";

    public SendMessageService(){
                this.messageService = NurigoApp.INSTANCE.initialize("NCSZCZ8ULCXH0PJM",
                        "4YKBPYQCSGYGKXOEQFMAGC3UBSILQ8GB", DOMAIN);
            }
    public void sendMessage(){
        Message message = new Message();
        message.setFrom("01071575997");  // 01012345678 형태여야 함.
        message.setTo("01040856453");
        message.setText("hi");
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);
        log.info("메시지가 전송되었습니다! ");
    }
}
