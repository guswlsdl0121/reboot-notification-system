package com.reboot_course.notification_system.domain.notification.service.ratelimit;

import com.reboot_course.notification_system.domain.notification.config.NotificationTestConfiguration;
import com.reboot_course.notification_system.domain.notification.repository.NotificationSubscriptionRepository;
import com.reboot_course.notification_system.domain.notification.service.NotificationService;
import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.ProductRepository;
import com.reboot_course.notification_system.domain.subscription.entity.NotificationSubscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@Import({NotificationTestConfiguration.class})
public abstract class AbstractRateLimitTest {
    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected NotificationSubscriptionRepository subscriptionRepository;

    @Autowired
    protected NotificationService notificationService;

    protected Product createAndSaveProduct() {
        Product product = Product.builder().quantity(100).restockVersion(0).build();
        return productRepository.save(product);
    }

    protected void createSubscriptions(Product product, int count) {
        for (int i = 0; i < count; i++) {
            NotificationSubscription subscription = NotificationSubscription.builder()
                    .userId((long) i)
                    .product(product)
                    .createdAt(LocalDateTime.now().plusSeconds(i))
                    .build();
            subscriptionRepository.save(subscription);
        }
    }

    long executeTest(Runnable task) {
        long startNano = System.nanoTime();
        task.run();
        long endNano = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(endNano - startNano);
    }

    protected void assertDuration(long duration, long expectedMinimumDuration) {
        log.info("duration: {}", duration);

        assertTrue(duration >= expectedMinimumDuration,
                String.format("실행 시간이 최소 %d ms 이상이어야 하지만, %d ms 입니다.", expectedMinimumDuration, duration));
    }

    protected void assertDuration(long duration, long expectedMinimumDuration, long tolerance) {
        long lowerBound = expectedMinimumDuration - tolerance;
        long upperBound = expectedMinimumDuration + tolerance;

        log.info("수행 시간: {}, 오차: {}", duration, tolerance);

        assertTrue(duration >= lowerBound && duration <= upperBound,
                String.format("실행 시간이 %d ms에서 %d ms 사이여야 하지만, 실제로는 %d ms 입니다.",
                        lowerBound, upperBound, duration));
    }
}
