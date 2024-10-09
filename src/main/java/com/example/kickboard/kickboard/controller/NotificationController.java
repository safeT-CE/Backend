package com.example.kickboard.kickboard.controller;

// SSE 방식 알림

import com.example.kickboard.kickboard.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe/{user_id}", produces = "text/event-stream;charset=UTF-8")
    public SseEmitter subscribe(@PathVariable(value = "user_id") Long userId) {
        return notificationService.subscribe(userId);
    }

    @PostMapping("/notify/{userId}")
    public void notify(@PathVariable("userId") Long userId, @RequestBody String message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("penalty")
                        .data(message, MediaType.TEXT_PLAIN));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }
    }
}

