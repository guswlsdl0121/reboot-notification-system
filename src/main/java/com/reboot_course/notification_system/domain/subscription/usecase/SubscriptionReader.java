package com.reboot_course.notification_system.domain.subscription.usecase;

import com.reboot_course.notification_system.common.exception.exception.NoSubscribersFoundException;
import com.reboot_course.notification_system.domain.subscription.entity.NotificationSubscription;
import com.reboot_course.notification_system.domain.subscription.repository.NotificationSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionReader {
    private final NotificationSubscriptionRepository subscriptionRepository;

    @Transactional(readOnly = true)
    public List<Long> getUserIdsForProduct(Long productId) {
        List<Long> userIds = subscriptionRepository.findAllByProductIdOrderByCreatedAtAsc(productId)
                .stream()
                .map(NotificationSubscription::getUserId)
                .toList();

        if (userIds.isEmpty()) {
            throw new NoSubscribersFoundException(productId);
        }

        return userIds;
    }
}
