package com.reboot_course.notification_system.service;

import com.reboot_course.notification_system.domain.history.entity.NotificationHistory;
import com.reboot_course.notification_system.domain.history.service.NotificationHistoryService;
import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.service.ProductService;
import com.reboot_course.notification_system.domain.subscriber.service.SubscriberService;
import com.reboot_course.notification_system.exception.NotificationException;
import com.reboot_course.notification_system.infra.cache.NotificationCacheManager;
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
    private final NotificationCacheManager cacheManager;

    private final ProductService productService;
    private final SubscriberService subscriberService;
    private final NotificationService notificationService;
    private final NotificationHistoryService historyService;

    public void sendNotifications(Long productId) {
        Product product = productService.fetchOneAndUpdateRestockCount(productId);
        List<Long> userIds = subscriberService.getUserIdsForProduct(productId);

        processNotifications(product, userIds);
    }

    public void resendRestockNotification(Long productId) {
        Product product = productService.fetchOne(productId);
        NotificationHistory errorHistory = historyService.getLastErrorHistory(productId);
        Long lastSentUserId = errorHistory.getLastSendUserId();
        List<Long> remainingUserIds = subscriberService.getUserIdsForProductException(productId, lastSentUserId);

        processNotifications(product, remainingUserIds);
    }

    private void processNotifications(Product product, List<Long> userIds) {
        cacheManager.initializeProcess(product);
        try {
            TaskProcessor taskProcessor = new TaskProcessor(product.getId(), userIds, notificationService::sendNotification);
            rateLimiter.process(taskProcessor);
            historyService.saveCompleted(product.getId());

        } catch (NotificationException e) {
            historyService.saveError(product.getId(), e.getLastUserId(), e.getStatus());
            log.error("failed for product: {}, user: {}, status: {}", product.getId(), e.getLastUserId(), e.getStatus(), e);

        } finally {
            cacheManager.finalizeProcess(product.getId());
        }
    }
}