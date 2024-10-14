package com.reboot_course.notification_system.common.exception.exception;

public class NoSubscribersFoundException extends RuntimeException {
    public NoSubscribersFoundException(Long productId) {
        super(String.format("해당 제품(id : %d)에 알림을 신청한 사용자가 없습니다.", productId));
    }
}