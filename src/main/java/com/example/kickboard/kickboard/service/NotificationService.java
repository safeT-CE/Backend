package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.exception.CustomException;
import com.example.kickboard.kickboard.repository.EmitterRepository;
import com.example.kickboard.login.entity.User;
import com.example.kickboard.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;

    private static final Long DEFAULT_TIMEOUT = 600L * 1000 * 60;

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = createEmitter(userId);

        sendToClient(userId, "EventStream Created. [userId="+ userId + "]", "sse 접속 성공");
        return emitter;
    }

    public void penaltyNotify(Long userId, String content, String date) {
        sendToClient(userId, content, date);
    }
    public void inquiryNotify(Long userId, String title, String response) {
        sendToClient(userId, title, response);
    }

    private void sendToClient(Long userId,String content, String response) {
        SseEmitter emitter = emitterRepository.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(userId))
                        .name("sse")
                        .data(content)
                        .comment(response));
            } catch (IOException e) {
                emitterRepository.deleteById(userId);
                emitter.completeWithError(e);
            }
        }
    }

//    private <T> void sendToClient(Long userId, T data, String comment, String type) {
//        SseEmitter emitter = emitterRepository.get(userId);
//        if (emitter != null) {
//            try {
//                emitter.send(SseEmitter.event()
//                        .id(String.valueOf(userId))
//                        .name(type)
//                        .data(data)
//                        .comment(comment));
//            } catch (IOException e) {
//                emitterRepository.deleteById(userId);
//                emitter.completeWithError(e);
//            }
//        }
//    }

    private SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));

        return emitter;
    }
}


//    private final NotificationController notificationController;
//
//    public NotificationService(NotificationController notificationController) {
//        this.notificationController = notificationController;
//    }
//
//    public void notifyUser(Long userId, String content,String response) {
//        log.info("You have received a penalty: " + response);    // 확인용
//        notificationController.notify(userId, "You have received a penalty: " + response);
//    }
//}
