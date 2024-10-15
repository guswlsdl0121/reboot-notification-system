package com.reboot_course.notification_system.domain.history.service;


import com.reboot_course.notification_system.domain.history.entity.NotificationHistory;
import com.reboot_course.notification_system.domain.history.entity.NotificationStatus;
import com.reboot_course.notification_system.domain.history.repository.cache.HistoryCacheRepository;
import com.reboot_course.notification_system.domain.history.repository.db.NotificationHistoryRepository;
import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.cache.ProductCachedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationHistoryService {
    private final NotificationHistoryRepository historyRepository;
    private final ProductCachedRepository productCachedRepository;
    private final HistoryCacheRepository historyCacheRepository;

    public NotificationHistory getOrCreateHistory(Long productId, Long userId) {
        NotificationHistory history = historyCacheRepository.get(productId);
        Product cachedProduct = productCachedRepository.get(productId);

        if (history != null) {
            return historyCacheRepository.updateLastReceiver(productId, userId);
        }

        NotificationHistory newHistory = NotificationHistory.builder()
                .product(cachedProduct)
                .restockVersion(cachedProduct.getRestockVersion())
                .notificationStatus(NotificationStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .lastSendUserId(userId)
                .build();

        historyCacheRepository.save(productId, newHistory);
        return newHistory;
    }

    public void saveCompleted(Long productId) {
        NotificationHistory history = historyCacheRepository.get(productId);
        history.completed();
        historyRepository.save(history);

        historyCacheRepository.delete(productId);
    }

    public void saveError(Long productId, Long lastUserId, NotificationStatus status) {
        NotificationHistory history = historyCacheRepository.get(productId);

        NotificationStatus.updateByException(status, history);
        history.updateLastReceiver(lastUserId);
        historyRepository.save(history);

        historyCacheRepository.delete(productId);
    }

    public NotificationHistory getLastErrorHistory(Long productId) {
        return historyRepository.findByProductIdAndNotificationStatusIn(productId,
                        Arrays.asList(NotificationStatus.CANCELED_BY_ERROR, NotificationStatus.CANCELED_BY_SOLD_OUT))
                .orElseThrow(() -> new RuntimeException("재발송 할 대상이 없습니다!"));
    }
}