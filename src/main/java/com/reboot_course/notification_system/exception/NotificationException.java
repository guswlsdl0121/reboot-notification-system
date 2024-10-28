package com.reboot_course.notification_system.exception;

import com.reboot_course.notification_system.domain.history.entity.NotificationStatus;
import lombok.Getter;

@Getter
public class NotificationException extends RuntimeException {
    private final Long lastUserId;
    private final NotificationStatus status;

    public NotificationException(String message, Long userId, NotificationStatus status) {
        super(message);
        this.lastUserId = userId;
        this.status = status;
    }

    public NotificationException(String message, Long userId, NotificationStatus status, Throwable cause) {
        super(message, cause);
        this.lastUserId = userId;
        this.status = status;
    }
}