package com.reboot_course.notification_system.domain.notification.service;

import com.reboot_course.notification_system.domain.notification.entity.NotificationHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogNotificationManager implements NotificationManager {
    public void send(NotificationHistory history) {
        log.info("Sending notification for product {} to user {}", history.getProductId(), history.getLastSendUserId());
    }
}
