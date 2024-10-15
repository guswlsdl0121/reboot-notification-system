package com.reboot_course.notification_system.domain.usernotification.repository.cache;

import com.reboot_course.notification_system.domain.usernotification.entity.NotificationCompleted;
import com.reboot_course.notification_system.domain.usernotification.repository.db.NotificationCompletedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BatchQueueRepository {
    private final NotificationsBatchQueue batchQueue;
    private final NotificationCompletedRepository notificationCompletedRepository;

    public void add(NotificationCompleted notification) {
        batchQueue.add(notification);
    }

    public List<NotificationCompleted> getAll() {
        return batchQueue.getAll();
    }

    public void saveAndClear() {
        List<NotificationCompleted> completedNotifications = batchQueue.getAll();
        if (!completedNotifications.isEmpty()) {
            notificationCompletedRepository.saveAll(completedNotifications);
            batchQueue.clear();
        }
    }

    public boolean isEmpty() {
        return batchQueue.isEmpty();
    }

    public void clear() {
        batchQueue.clear();
    }
}