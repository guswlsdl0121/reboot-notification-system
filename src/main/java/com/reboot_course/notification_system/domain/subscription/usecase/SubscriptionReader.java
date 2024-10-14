package com.reboot_course.notification_system.domain.subscription.usecase;

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
            throw new IllegalStateException(String.format("현재 해당 상품의 알림 신청자가 없습니다. (id : %d)", productId));
        }

        return userIds;
    }
}
