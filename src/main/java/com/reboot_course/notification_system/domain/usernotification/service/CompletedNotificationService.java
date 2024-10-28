package com.reboot_course.notification_system.domain.usernotification.service;

import com.reboot_course.notification_system.domain.history.entity.NotificationHistory;
import com.reboot_course.notification_system.domain.usernotification.entity.NotificationCompleted;
import com.reboot_course.notification_system.domain.usernotification.repository.cache.BatchQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompletedNotificationService {
    private final BatchQueueRepository batchQueueRepository;

    public void addCompletedNotification(NotificationHistory history, Long userId) {
        NotificationCompleted completedNotification = NotificationCompleted.builder()
                .createdAt(LocalDateTime.now())
                .userId(userId)
                .restockVersion(history.getRestockVersion())
                .build();
        batchQueueRepository.add(completedNotification);
    }

    public List<NotificationCompleted> getAllCompletedNotifications() {
        return batchQueueRepository.getAll();
    }

    public void saveAndClearCompletedNotifications() {
        batchQueueRepository.saveAndClear();
    }
}
