package com.example.safeT.kickboard.service;

import com.example.safeT.kickboard.controller.NotificationController;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {
    private final NotificationController notificationController;

    public NotificationService(NotificationController notificationController) {
        this.notificationController = notificationController;
    }

    public void notifyUser(Long userId, String message) {
        log.info("You have received a penalty: " + message);    // 확인용
        notificationController.notify(userId, "You have received a penalty: " + message);
    }
}
