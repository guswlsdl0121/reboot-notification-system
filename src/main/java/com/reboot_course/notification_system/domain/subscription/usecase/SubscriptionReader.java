package com.reboot_course.notification_system.domain.subscription.usecase;

import com.reboot_course.notification_system.domain.notification.repository.NotificationSubscriptionRepository;
import com.reboot_course.notification_system.domain.subscription.entity.NotificationSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionReader {
    private final NotificationSubscriptionRepository subscriptionRepository;

    @Transactional
    public List<Long> getUserIdsForProduct(Long productId) {
        return subscriptionRepository.findAllByProductIdOrderByCreatedAtAsc(productId)
                .stream()
                .map(NotificationSubscription::getUserId)
                .toList();
    }
}
