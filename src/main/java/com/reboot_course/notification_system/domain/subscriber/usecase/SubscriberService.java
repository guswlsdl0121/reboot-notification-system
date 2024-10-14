package com.reboot_course.notification_system.domain.subscriber.usecase;

import com.reboot_course.notification_system.domain.subscriber.entity.Subscriber;
import com.reboot_course.notification_system.domain.subscriber.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriberService {
    private final SubscriberRepository subscriptionRepository;

    @Transactional(readOnly = true)
    public List<Long> getUserIdsForProduct(Long productId) {
        List<Long> userIds = subscriptionRepository.findAllByProductIdOrderByCreatedAtAsc(productId)
                .stream()
                .map(Subscriber::getUserId)
                .toList();

        if (userIds.isEmpty()) {
            throw new IllegalStateException(String.format("현재 해당 상품의 알림 신청자가 없습니다. (id : %d)", productId));
        }

        return userIds;
    }
}
