package com.reboot_course.notification_system.domain.history.service;

import com.reboot_course.notification_system.domain.history.entity.NotificationHistory;
import com.reboot_course.notification_system.domain.history.entity.NotificationStatus;
import com.reboot_course.notification_system.domain.history.repository.cache.HistoryCache;
import com.reboot_course.notification_system.domain.history.repository.cache.HistoryCacheRepository;
import com.reboot_course.notification_system.domain.history.repository.db.NotificationHistoryRepository;
import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.cache.ProductCachedRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;

@Slf4j
@Transactional
@SpringBootTest
@ActiveProfiles("test")
class NotificationHistoryIntegrationServiceTest {
    @Autowired
    private HistoryCacheRepository historyCacheRepository;

    @Autowired
    private NotificationHistoryRepository notificationHistoryRepository;

    @Autowired
    private NotificationHistoryService notificationHistoryService;

    @Autowired
    private ProductCachedRepository productCachedRepository;

    @Autowired
    private HistoryCache historyCache;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        historyCacheRepository.clear();
        productCachedRepository.clear();

        testProduct = Product.builder()
                .id(1L)
                .restockVersion(1)
                .build();
        productCachedRepository.save(testProduct.getId(), testProduct);
    }

    @Test
    @DisplayName("최초 실행 시, 만약 상품에 대한 히스토리가 캐시에 없다면 생성하고 캐시에 저장한다.")
    void testGetOrCreateHistory() {
        // When
        NotificationHistory history = notificationHistoryService.getOrCreateHistory(testProduct.getId(), 100L);

        // 캐시에는 새로운 history가 저장되어야 함
        assertEquals(testProduct.getId(), history.getProduct().getId());
        assertEquals(100L, history.getLastSendUserId());
        assertEquals(NotificationStatus.IN_PROGRESS, history.getNotificationStatus());

        // 아직 디비에는 반영되면 안됨.
        assertEquals(0, notificationHistoryRepository.count());
    }

    @Test
    @DisplayName("알림이 다 전송되고 COMPLETED 상태로 저장하면, 캐시에서 히스토리를 지우고 DB에 저장해야 한다.")
    void testSaveCompleted() {
        // Given
        NotificationHistory history = notificationHistoryService.getOrCreateHistory(testProduct.getId(), 100L);

        // When
        notificationHistoryService.saveCompleted(testProduct.getId());

        // Then
        NotificationHistory savedHistory = notificationHistoryRepository.findByProduct_Id(testProduct.getId()).orElse(null);
        assertNotNull(savedHistory);
        assertEquals(NotificationStatus.COMPLETED, savedHistory.getNotificationStatus());
        assertNull(historyCache.get(savedHistory.getId()));

        // 실제 리포지토리 확인
        NotificationHistory repoHistory = notificationHistoryRepository.findById(history.getId()).orElse(null);
        assertNotNull(repoHistory);
        assertEquals(NotificationStatus.COMPLETED, repoHistory.getNotificationStatus());
    }

    @Test
    @DisplayName("알림이 중간에 예외로 멈추면, 캐시에서 히스토리를 지우고 DB에 저장해야 한다.")
    void testSaveError() {
        // Given
        NotificationHistory history = notificationHistoryService.getOrCreateHistory(testProduct.getId(), 100L);

        // When
        notificationHistoryService.saveError(testProduct.getId(), 200L, NotificationStatus.CANCELED_BY_ERROR);

        // Then
        NotificationHistory savedHistory = notificationHistoryRepository.findByProduct_Id(testProduct.getId()).orElse(null);
        assertNotNull(savedHistory);
        assertEquals(NotificationStatus.CANCELED_BY_ERROR, savedHistory.getNotificationStatus());
        assertEquals(200L, savedHistory.getLastSendUserId());
        assertNull(historyCache.get(savedHistory.getId()));

        // 실제 리포지토리 확인
        NotificationHistory repoHistory = notificationHistoryRepository.findById(history.getId()).orElse(null);
        assertNotNull(repoHistory);
        assertEquals(NotificationStatus.CANCELED_BY_ERROR, repoHistory.getNotificationStatus());
        assertEquals(200L, repoHistory.getLastSendUserId());
    }

    @Test
    @DisplayName("에러로 저장된 히스토리를 조회하여, 마지막으로 보낸 사용자들을 찾아야 한다.")
    void testGetLastErrorHistory() {
        // Given
        NotificationHistory history = notificationHistoryService.getOrCreateHistory(testProduct.getId(), 100L);
        notificationHistoryService.saveError(testProduct.getId(), 200L, NotificationStatus.CANCELED_BY_ERROR);

        // When
        NotificationHistory lastErrorHistory = notificationHistoryService.getLastErrorHistory(testProduct.getId());

        // Then
        assertNotNull(lastErrorHistory);
        assertEquals(NotificationStatus.CANCELED_BY_ERROR, lastErrorHistory.getNotificationStatus());
        assertEquals(200L, lastErrorHistory.getLastSendUserId());

        // 실제 리포지토리 확인
        NotificationHistory repoHistory = notificationHistoryRepository.findById(history.getId()).orElse(null);
        assertNotNull(repoHistory);
        assertEquals(NotificationStatus.CANCELED_BY_ERROR, repoHistory.getNotificationStatus());
        assertEquals(200L, repoHistory.getLastSendUserId());
    }
}