package com.reboot_course.notification_system.domain.notification.service;

import com.reboot_course.notification_system.domain.notification.entity.NotificationHistory;

public interface NotificationManager {
    void send(NotificationHistory history);
}
