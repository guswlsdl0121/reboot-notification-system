package com.reboot_course.notification_system.domain.product.cache;

import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.cache.QuantityCache;
import com.reboot_course.notification_system.domain.product.repository.cache.QuantityCacheRepository;
import com.reboot_course.notification_system.domain.product.repository.db.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Import({QuantityCache.class, QuantityCacheRepository.class})
class QuantityCacheRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QuantityCacheRepository quantityCacheRepository;

    @Test
    void t1() throws InterruptedException {
        // 제품 초기화
        Product product = Product.builder()
                .quantity(100)
                .restockVersion(1)
                .build();
        product = productRepository.save(product);
        Long productId = product.getId();

        // 캐시 초기화
        quantityCacheRepository.save(productId, 100);

        // 10번의 구매 시뮬레이션
        for (int i = 0; i < 10; i++) {
            // 구매 로직
            Product dbProduct = productRepository.findById(productId).orElseThrow();
            boolean decreased = dbProduct.decreaseQuantity();
            if (decreased) {
                productRepository.save(dbProduct);
            }
        }

        Thread.sleep(150);

        // 최종 상태 확인
        Product finalProduct = productRepository.findById(productId).orElseThrow();
        int finalDbQuantity = finalProduct.getQuantity();
        int finalCachedQuantity = quantityCacheRepository.get(productId);

        // 검증
        assertThat(finalDbQuantity).isEqualTo(90);
        assertThat(finalCachedQuantity).isEqualTo(90);
    }
}