package com.reboot_course.notification_system.domain.notification.usecase;


import com.reboot_course.notification_system.domain.notification.entity.NotificationHistory;
import com.reboot_course.notification_system.domain.notification.entity.NotificationStatus;
import com.reboot_course.notification_system.domain.notification.repository.NotificationHistoryRepository;
import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.cache.ProductCachedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationHistoryService {
    private final ProductCachedRepository productCachedRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;

    public NotificationHistory createHistory(Long productId, Long userId) {
        Product cachedProduct = productCachedRepository.get(productId);

        NotificationStatus initialStatus = cachedProduct.getQuantity() > 0
                ? NotificationStatus.IN_PROGRESS
                : NotificationStatus.CANCELED_BY_SOLD_OUT;

        return NotificationHistory.builder()
                .product(cachedProduct)
                .restockVersion(cachedProduct.getRestockVersion())
                .notificationStatus(initialStatus)
                .createdAt(LocalDateTime.now())
                .lastSendUserId(userId)
                .build();
    }

    @Transactional
    public void completeHistory(NotificationHistory history) {
        history.completed();
        saveHistory(history);
    }

    @Transactional
    public void cancelHistoryByError(NotificationHistory history) {
        history.cancelByError();
        saveHistory(history);
    }

    @Transactional
    public void saveHistory(NotificationHistory history) {
        notificationHistoryRepository.save(history);
    }
}