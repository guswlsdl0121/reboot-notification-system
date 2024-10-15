package com.reboot_course.notification_system.domain.notification.usecase;

import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.db.ProductRepository;
import com.reboot_course.notification_system.domain.subscriber.entity.Subscriber;
import com.reboot_course.notification_system.domain.subscriber.repository.SubscriberRepository;
import com.reboot_course.notification_system.domain.subscriber.service.SubscriberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(SubscriberService.class)
class SubscriberServiceTest {
    @Autowired
    private SubscriberRepository subscriptionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubscriberService subscriberService;

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
        Subscriber sub1 = createSubscription(1L, LocalDateTime.now().minusDays(2));
        Subscriber sub2 = createSubscription(2L, LocalDateTime.now().minusDays(1));
        Subscriber sub3 = createSubscription(3L, LocalDateTime.now());

        subscriptionRepository.saveAll(List.of(sub1, sub2, sub3));

        // When
        List<Long> result = subscriberService.getUserIdsForProduct(testProduct.getId());

        // Then
        assertEquals(3, result.size());
        assertEquals(List.of(1L, 2L, 3L), result);
    }

    @Test
    @DisplayName("특정 상품의 알림 구독자가 없는 경우 예외를 던져야 한다.")
    void t2() {
        // When & Then
        assertThrows(IllegalStateException.class, () ->
                subscriberService.getUserIdsForProduct(testProduct.getId())
        );
    }

    private Subscriber createSubscription(Long userId, LocalDateTime createdAt) {
        return Subscriber.builder()
                .userId(userId)
                .product(testProduct)
                .createdAt(createdAt)
                .build();
    }
}