package com.reboot_course.notification_system.domain.notification.service;

import com.reboot_course.notification_system.common.ratelimit.RateLimiter;
import com.reboot_course.notification_system.common.ratelimit.TaskProcessor;
import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.service.ProductService;
import com.reboot_course.notification_system.domain.subscriber.usecase.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceFacade {
    private final RateLimiter rateLimiter;

    private final ProductService productService;
    private final SubscriberService subscriberService;
    private final NotificationService notificationService;

    public void sendNotifications(Long productId) {
        Product product = productService.fetchOneAndUpdateRestockCount(productId);
        List<Long> userIds = subscriberService.getUserIdsForProduct(productId);

        rateLimiter.process(
                new TaskProcessor(product.getId(), userIds, notificationService::sendNotification)
        );
    }
}