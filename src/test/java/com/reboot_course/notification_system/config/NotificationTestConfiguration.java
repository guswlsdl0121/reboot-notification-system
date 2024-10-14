package com.reboot_course.notification_system.config;

import com.reboot_course.notification_system.common.ratelimit.QueueRateLimiter;
import com.reboot_course.notification_system.common.ratelimit.RateLimiter;
import com.reboot_course.notification_system.domain.notification.repository.NotificationHistoryRepository;
import com.reboot_course.notification_system.domain.notification.service.NotificationManager;
import com.reboot_course.notification_system.domain.notification.service.NotificationService;
import com.reboot_course.notification_system.domain.notification.service.NotificationServiceFacade;
import com.reboot_course.notification_system.domain.notification.usecase.NotificationHistoryService;
import com.reboot_course.notification_system.domain.product.repository.cache.ProductCachedRepository;
import com.reboot_course.notification_system.domain.product.repository.db.ProductRepository;
import com.reboot_course.notification_system.domain.product.service.ProductService;
import com.reboot_course.notification_system.domain.subscriber.repository.SubscriberRepository;
import com.reboot_course.notification_system.domain.subscriber.usecase.SubscriberService;
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
    public NotificationServiceFacade notificationService(
            RateLimiter rateLimiter,
            SubscriberService subscriberService,
            ProductService productService,
            NotificationService notificationService) {
        return new NotificationServiceFacade(rateLimiter, productService, subscriberService, notificationService);
    }
}
