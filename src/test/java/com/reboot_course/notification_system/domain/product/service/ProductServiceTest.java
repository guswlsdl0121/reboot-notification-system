package com.reboot_course.notification_system.domain.product.service;

import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.db.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({ProductService.class})
class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("제품을 가져오면서 리스탁 버전도 업데이트해야 한다.")
    void t1() {
        // Given
        Long productId = 1L;
        Product product = createProduct(productId, 10);

        int initialVersion = product.getRestockVersion();

        // When
        Product result = productService.fetchOneAndUpdateRestockCount(productId);

        // Then
        assertEquals(initialVersion + 1, result.getRestockVersion());
    }

    @Test
    @DisplayName("id로 엔티티를 못찾으면, 예외를 던져야 한다.")
    void t2() {
        assertThrows(EntityNotFoundException.class, () -> productService.fetchOneAndUpdateRestockCount(9909L));
    }

    @Test
    @DisplayName("만약 재고가 없다면, 예외를 던져야 한다.")
    void t3() {
        createProduct(2L, 0);
        assertThrows(IllegalStateException.class, () -> productService.fetchOneAndUpdateRestockCount(2L));
    }

    private Product createProduct(Long productId, int quantity) {
        Product product = Product.builder()
                .id(productId)
                .quantity(quantity)
                .restockVersion(0)
                .build();
        return productRepository.save(product);
    }
}