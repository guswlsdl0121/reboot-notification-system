package com.reboot_course.notification_system.domain.notification.service;

import com.reboot_course.notification_system.common.ratelimit.RateLimiter;
import com.reboot_course.notification_system.common.ratelimit.TaskProcessor;
import com.reboot_course.notification_system.domain.subscription.usecase.SubscriptionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final RateLimiter rateLimiter;
    private final SubscriptionReader subscriptionReader;

    public void sendNotifications(Long productId) {
        List<Long> userIds = subscriptionReader.getUserIdsForProduct(productId);
        rateLimiter.process(new TaskProcessor(productId, userIds, this::sendNotification));
    }

    @Transactional
    public void sendNotification(Long productId, Long userId) {
        System.out.println("Sending notification for product " + productId + " to user " + userId);
    }
}