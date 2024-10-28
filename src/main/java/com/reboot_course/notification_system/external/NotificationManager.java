package com.reboot_course.notification_system.external;

import com.reboot_course.notification_system.domain.history.entity.NotificationHistory;

public interface NotificationManager {
    void send(NotificationHistory history);
}
