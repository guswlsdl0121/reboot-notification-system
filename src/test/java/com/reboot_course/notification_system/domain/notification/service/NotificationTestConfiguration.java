package com.reboot_course.notification_system.domain.notification.service;

import com.reboot_course.notification_system.common.ratelimit.QueueRateLimiter;
import com.reboot_course.notification_system.common.ratelimit.RateLimiter;
import com.reboot_course.notification_system.domain.notification.repository.NotificationSubscriptionRepository;
import com.reboot_course.notification_system.domain.subscription.usecase.SubscriptionReader;
import org.springframework.context.annotation.Bean;

public class NotificationTestConfiguration {
    @Bean
    public RateLimiter rateLimiter() {
        return new QueueRateLimiter();
    }

    @Bean
    public SubscriptionReader subscriptionReader(NotificationSubscriptionRepository repository) {
        return new SubscriptionReader(repository);
    }

    @Bean
    public NotificationService notificationService(RateLimiter rateLimiter, SubscriptionReader subscriptionReader) {
        return new NotificationService(rateLimiter, subscriptionReader);
    }
}
