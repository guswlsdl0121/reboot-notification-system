package com.reboot_course.notification_system.domain.notification.service;

import com.reboot_course.notification_system.common.ratelimit.RateLimiter;
import com.reboot_course.notification_system.common.ratelimit.TaskProcessor;
import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.usecase.ProductFinder;
import com.reboot_course.notification_system.domain.subscriber.usecase.SubscriberReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final RateLimiter rateLimiter;
    private final SubscriberReader subscriberReader;
    private final ProductFinder productFinder;

    public void sendNotifications(Long productId) {
        Product product = productFinder.fetchOneAndUpdateRestockCount(productId);
        List<Long> userIds = subscriberReader.getUserIdsForProduct(productId);

        rateLimiter.process(new TaskProcessor(productId, userIds, this::sendNotification));
    }

    @Transactional
    public void sendNotification(Long productId, Long userId) {
        System.out.println("Sending notification for product " + productId + " to user " + userId);
    }
}