package com.reboot_course.notification_system.domain.notification.controller;

import com.reboot_course.notification_system.domain.notification.service.NotificationServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationServiceFacade notificationServiceFacade;


    @PostMapping("/{productId}/notifications/re-stock")
    public ResponseEntity<String> sendNotification(@PathVariable Long productId) {
        notificationServiceFacade.sendNotifications(productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/{productId}/notifications/re-stock")
    public ResponseEntity<String> resendNotification(@PathVariable Long productId) {
        // TODO: 관리자용 재입고 알림 재전송 로직 구현
        // notificationService.resendRestockNotification(productId);
        return ResponseEntity.ok().build();
    }
}
