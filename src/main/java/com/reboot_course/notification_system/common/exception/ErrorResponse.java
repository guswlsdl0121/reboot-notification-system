package com.reboot_course.notification_system.common.exception;

public record ErrorResponse(int status, String error, String message) {
}
