package com.reboot_course.notification_system.domain.usernotification.repository.cache;

import com.reboot_course.notification_system.domain.usernotification.entity.NotificationCompleted;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class NotificationsBatchQueue {
    private final ConcurrentLinkedQueue<NotificationCompleted> notifications;

    public NotificationsBatchQueue() {
        this.notifications = new ConcurrentLinkedQueue<>();
    }

    public void add(NotificationCompleted notification) {
        notifications.offer(notification);
    }

    public List<NotificationCompleted> getAll() {
        return new ArrayList<>(notifications);
    }

    public void clear() {
        notifications.clear();
    }

    public boolean isEmpty() {
        return notifications.isEmpty();
    }
}