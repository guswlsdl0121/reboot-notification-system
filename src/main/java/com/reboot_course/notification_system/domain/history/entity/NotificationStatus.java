package com.reboot_course.notification_system.domain.history.entity;

public enum NotificationStatus {
    IN_PROGRESS, CANCELED_BY_SOLD_OUT, CANCELED_BY_ERROR, COMPLETED;

    public static void updateByException(NotificationStatus status, NotificationHistory history) {
        if (status == NotificationStatus.CANCELED_BY_SOLD_OUT) {
            history.cancelBySoldOut();
        } else {
            history.cancelByError();
        }
    }
}
