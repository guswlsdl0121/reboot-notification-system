package com.reboot_course.notification_system.exception;

public record ErrorResponse(int status, String error, String message) {
}
