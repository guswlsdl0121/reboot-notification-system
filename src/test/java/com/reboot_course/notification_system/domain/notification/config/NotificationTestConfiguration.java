package com.reboot_course.notification_system.domain.notification.config;

import com.reboot_course.notification_system.common.ratelimit.QueueRateLimiter;
import com.reboot_course.notification_system.common.ratelimit.RateLimiter;
import com.reboot_course.notification_system.domain.notification.service.NotificationService;
import com.reboot_course.notification_system.domain.product.repository.ProductRepository;
import com.reboot_course.notification_system.domain.product.usecase.ProductFinder;
import com.reboot_course.notification_system.domain.subscriber.repository.SubscriberRepository;
import com.reboot_course.notification_system.domain.subscriber.usecase.SubscriberReader;
import org.springframework.context.annotation.Bean;

public class NotificationTestConfiguration {
    @Bean
    public RateLimiter rateLimiter() {
        return new QueueRateLimiter();
    }

    @Bean
    public SubscriberReader subscriptionReader(SubscriberRepository repository) {
        return new SubscriberReader(repository);
    }

    @Bean
    public ProductFinder productFinder(ProductRepository repository) {
        return new ProductFinder(repository);
    }

    @Bean
    public NotificationService notificationService(
            RateLimiter rateLimiter,
            SubscriberReader subscriberReader,
            ProductFinder productFinder) {
        return new NotificationService(rateLimiter, subscriberReader, productFinder);
    }
}
