package com.reboot_course.notification_system.domain.notification.service;

import com.reboot_course.notification_system.domain.notification.entity.NotificationHistory;
import com.reboot_course.notification_system.domain.notification.entity.NotificationStatus;
import com.reboot_course.notification_system.domain.notification.usecase.NotificationHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationHistoryService historyService;
    private final NotificationManager notificationManager;

    @Transactional
    public void sendNotification(Long productId, Long userId) {
        NotificationHistory history = historyService.createHistory(productId, userId);

        try {
            if (history.getNotificationStatus() == NotificationStatus.IN_PROGRESS) {
                notificationManager.send(history);
                historyService.completeHistory(history);
            }
        } catch (Exception e) {
            historyService.cancelHistoryByError(history);
        }
    }
}