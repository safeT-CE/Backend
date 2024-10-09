package com.example.kickboard.kickboard.service;

import com.example.kickboard.kickboard.repository.EmitterRepository;
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

        sendToClient(userId, "success","EventStream Created. [userId="+ userId + "]", "sse 접속 성공");
        return emitter;
    }

    public void penaltyNotify(Long userId, String content, String date) {
        sendToClient(userId, "penalty", content, date);
    }

    public void inquiryNotify(Long userId, String title, String response) {
        log.info("NotificationService : inquiry");
        sendToClient(userId, "inquiry", title, response);
    }

    private void sendToClient(Long userId, String type, String content, String response) {
        log.info("NotificationService : inquiry2");
        SseEmitter emitter = emitterRepository.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(userId))
                        .name("sse")
                        .data(type)
                        .comment(content));
            } catch (IOException e) {
                emitterRepository.deleteById(userId);
                emitter.completeWithError(e);
            }
        }
    }

    private SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));

        return emitter;
    }
}
