package com.reboot_course.notification_system.config;

import com.reboot_course.notification_system.domain.history.repository.db.NotificationHistoryRepository;
import com.reboot_course.notification_system.domain.history.service.NotificationHistoryService;
import com.reboot_course.notification_system.domain.notification.service.NotificationService;
import com.reboot_course.notification_system.domain.product.repository.cache.ProductCachedRepository;
import com.reboot_course.notification_system.domain.product.repository.db.ProductRepository;
import com.reboot_course.notification_system.domain.product.service.ProductService;
import com.reboot_course.notification_system.domain.subscriber.repository.SubscriberRepository;
import com.reboot_course.notification_system.domain.subscriber.service.SubscriberService;
import com.reboot_course.notification_system.external.NotificationManager;
import com.reboot_course.notification_system.infra.ratelimit.RateLimiter;
import com.reboot_course.notification_system.infra.ratelimit.impl.QueueRateLimiter;
import com.reboot_course.notification_system.service.NotificationSystemService;
import org.springframework.context.annotation.Bean;

public class NotificationTestConfiguration {
    @Bean
    public RateLimiter rateLimiter() {
        return new QueueRateLimiter();
    }

    @Bean
    public SubscriberService subscriptionReader(SubscriberRepository repository) {
        return new SubscriberService(repository);
    }

    @Bean
    public NotificationService notificationService(NotificationHistoryService historyService, NotificationManager manager) {
        return new NotificationService(historyService, manager);
    }

    @Bean
    NotificationHistoryService notificationHistoryService(NotificationHistoryRepository historyRepository, ProductCachedRepository cachedRepository) {
        return new NotificationHistoryService(cachedRepository, historyRepository);
    }

    @Bean
    public ProductService productFinder(ProductRepository repository) {
        return new ProductService(repository);
    }

    @Bean
    public NotificationSystemService notificationService(
            RateLimiter rateLimiter,
            SubscriberService subscriberService,
            ProductService productService,
            NotificationService notificationService) {
        return new NotificationSystemService(rateLimiter, productService, subscriberService, notificationService);
    }
}
