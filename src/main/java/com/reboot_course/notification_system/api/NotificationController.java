package com.reboot_course.notification_system.api;

import com.reboot_course.notification_system.service.NotificationSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationSystemService notificationSystemService;


    @PostMapping("/{productId}/notifications/re-stock")
    public ResponseEntity<String> sendNotification(@PathVariable Long productId) {
        notificationSystemService.sendNotifications(productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/{productId}/notifications/re-stock")
    public ResponseEntity<String> resendNotification(@PathVariable Long productId) {
        notificationSystemService.resendRestockNotification(productId);
        return ResponseEntity.ok().build();
    }
}
