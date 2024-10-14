package com.reboot_course.notification_system.common.exception.dto;

public record ErrorResponse(int status, String error, String message) {
}
