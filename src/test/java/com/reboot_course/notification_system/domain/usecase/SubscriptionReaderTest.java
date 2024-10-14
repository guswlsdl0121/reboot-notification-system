package com.reboot_course.notification_system.domain.usecase;

import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.ProductRepository;
import com.reboot_course.notification_system.domain.subscription.entity.NotificationSubscription;
import com.reboot_course.notification_system.domain.subscription.repository.NotificationSubscriptionRepository;
import com.reboot_course.notification_system.domain.subscription.usecase.SubscriptionReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(SubscriptionReader.class)
class SubscriptionReaderTest {
    @Autowired
    private NotificationSubscriptionRepository subscriptionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubscriptionReader subscriptionReader;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .quantity(100)
                .restockVersion(1)
                .build();
        productRepository.save(testProduct);
    }

    @Test
    @DisplayName("특정 상품의 알림 구독자의 userId를 순차적으로 조회해야 한다.")
    void t1() {
        // Given
        NotificationSubscription sub1 = createSubscription(1L, LocalDateTime.now().minusDays(2));
        NotificationSubscription sub2 = createSubscription(2L, LocalDateTime.now().minusDays(1));
        NotificationSubscription sub3 = createSubscription(3L, LocalDateTime.now());

        subscriptionRepository.saveAll(List.of(sub1, sub2, sub3));

        // When
        List<Long> result = subscriptionReader.getUserIdsForProduct(testProduct.getId());

        // Then
        assertEquals(3, result.size());
        assertEquals(List.of(1L, 2L, 3L), result);
    }

    @Test
    @DisplayName("특정 상품의 알림 구독자가 없는 경우 예외를 던져야 한다.")
    void t2() {
        // When & Then
        assertThrows(IllegalStateException.class, () ->
                subscriptionReader.getUserIdsForProduct(testProduct.getId())
        );
    }

    private NotificationSubscription createSubscription(Long userId, LocalDateTime createdAt) {
        return NotificationSubscription.builder()
                .userId(userId)
                .product(testProduct)
                .createdAt(createdAt)
                .build();
    }
}