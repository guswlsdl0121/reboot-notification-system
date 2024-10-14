package com.reboot_course.notification_system.domain.product.cache;

import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.cache.ProductCache;
import com.reboot_course.notification_system.domain.product.repository.cache.ProductCachedRepository;
import com.reboot_course.notification_system.domain.product.repository.db.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Import({ProductCache.class, ProductCachedRepository.class})
class ProductCacheRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCachedRepository productCachedRepository;

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
        productCachedRepository.save(productId, product);

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
        Product finalCachedProduct = productCachedRepository.get(productId);

        // 검증
        assertThat(finalDbQuantity).isEqualTo(finalCachedProduct.getQuantity());
        assertThat(finalCachedProduct.getQuantity()).isEqualTo(90);
    }
}