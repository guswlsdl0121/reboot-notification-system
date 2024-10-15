package com.reboot_course.notification_system.service;

import com.reboot_course.notification_system.domain.history.service.NotificationHistoryService;
import com.reboot_course.notification_system.domain.notification.service.NotificationService;
import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.service.ProductService;
import com.reboot_course.notification_system.domain.subscriber.service.SubscriberService;
import com.reboot_course.notification_system.exception.NotificationException;
import com.reboot_course.notification_system.infra.ratelimit.RateLimiter;
import com.reboot_course.notification_system.infra.ratelimit.TaskProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationSystemService {
    private final RateLimiter rateLimiter;

    private final ProductService productService;
    private final SubscriberService subscriberService;
    private final NotificationService notificationService;
    private final NotificationHistoryService historyService;

    public void sendNotifications(Long productId) {
        Product product = productService.fetchOneAndUpdateRestockCount(productId);
        List<Long> userIds = subscriberService.getUserIdsForProduct(productId);

        try {
            rateLimiter.process(
                    new TaskProcessor(product.getId(), userIds, notificationService::sendNotification)
            );
            historyService.saveCompleted(productId);
        } catch (NotificationException e) {
            historyService.saveError(productId, e.getLastUserId(), e.getStatus());
            log.error("Notification failed for product: {}, user: {}, status: {}", productId, e.getLastUserId(), e.getStatus(), e);
        }
    }
}