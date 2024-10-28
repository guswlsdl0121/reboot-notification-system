package com.reboot_course.notification_system.exception;

import com.reboot_course.notification_system.domain.history.entity.NotificationStatus;

public class ProductSoldOutException extends NotificationException {
    public ProductSoldOutException(String message, Long userId, NotificationStatus status) {
        super(message, userId, status);
    }
}