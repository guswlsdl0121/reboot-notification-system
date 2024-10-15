package com.reboot_course.notification_system.domain.notification.service;

import com.reboot_course.notification_system.domain.history.entity.NotificationHistory;
import com.reboot_course.notification_system.domain.history.entity.NotificationStatus;
import com.reboot_course.notification_system.domain.history.service.NotificationHistoryService;
import com.reboot_course.notification_system.exception.NotificationException;
import com.reboot_course.notification_system.exception.ProductSoldOutException;
import com.reboot_course.notification_system.external.NotificationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationHistoryService historyService;
    private final NotificationManager notificationManager;

    public void sendNotification(Long productId, Long userId) throws NotificationException {
        NotificationHistory history = historyService.getOrCreateHistory(productId, userId);

        try {
            history.determineStatus();
            if (history.isSoldOut()) {
                throw new ProductSoldOutException("Product sold out: " + productId, userId, NotificationStatus.CANCELED_BY_SOLD_OUT);
            }
            notificationManager.send(history);
        } catch (Exception e) {
            throw new NotificationException("Error sending notification", userId, NotificationStatus.CANCELED_BY_ERROR, e);
        }
    }
}