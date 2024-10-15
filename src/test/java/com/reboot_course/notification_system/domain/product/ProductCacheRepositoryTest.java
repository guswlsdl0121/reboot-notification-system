package com.reboot_course.notification_system.domain.product;

import com.reboot_course.notification_system.config.SchedulerConfig;
import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.cache.ProductCachedRepository;
import com.reboot_course.notification_system.domain.product.repository.db.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@ActiveProfiles("test")
class ProductCacheRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCachedRepository productCachedRepository;

    @Test
    @DisplayName("스케쥴러를 통해서 캐시가 변경된 수량을 잘 가져오는지 확인")
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
        productCachedRepository.startSyncScheduler(100);

        // 10번의 구매 시뮬레이션
        Product dbProduct = productRepository.findById(productId).orElseThrow();
        for (int i = 0; i < 10; i++) {
            boolean decreased = dbProduct.decreaseQuantity();
            if (decreased) {
                productRepository.save(dbProduct);
            }
        }

        //stopSync가 된 시점에는 스케쥴링을 종료해야됨. 따라서 변화 X
        productCachedRepository.stopSyncScheduler();
        dbProduct.decreaseQuantity();

        Thread.sleep(500);

        // 최종 상태 확인
        Product finalProduct = productRepository.findById(productId).orElseThrow();
        int finalDbQuantity = finalProduct.getQuantity();
        Product finalCachedProduct = productCachedRepository.get(productId);

        // 검증
        assertThat(finalDbQuantity).isEqualTo(finalCachedProduct.getQuantity());
        assertThat(finalCachedProduct.getQuantity()).isEqualTo(90);
    }
}