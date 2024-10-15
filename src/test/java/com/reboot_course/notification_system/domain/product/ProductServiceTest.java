package com.reboot_course.notification_system.domain.product;

import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.db.ProductRepository;
import com.reboot_course.notification_system.domain.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

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
        Product product = createProduct(10);

        int initialVersion = product.getRestockVersion();

        // When
        Product result = productService.fetchOneAndUpdateRestockCount(product.getId());

        // Then
        assertEquals(initialVersion + 1, result.getRestockVersion());
    }

    @Test
    @DisplayName("fetchOne은 제품을 가져오지만 리스탁 버전은 업데이트하지 않아야 한다.")
    void t2() {
        // Given
        Product product = createProduct(10);

        int initialVersion = product.getRestockVersion();

        // When
        Product result = productService.fetchOne(product.getId());

        // Then
        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(initialVersion, result.getRestockVersion());
        assertEquals(10, result.getQuantity());
    }

    @Test
    @DisplayName("만약 재고가 없다면, 예외를 던져야 한다.")
    void t3() {
        Product product = createProduct(0);
        assertThrows(IllegalStateException.class, () -> productService.fetchOneAndUpdateRestockCount(product.getId()));
    }

    @Test
    @DisplayName("id로 엔티티를 못찾으면, 예외를 던져야 한다.")
    void t4() {
        assertThrows(EntityNotFoundException.class, () -> productService.fetchOneAndUpdateRestockCount(9909L));
    }

    @Test
    @DisplayName("fetchOne 메서드도 재고가 없으면 예외를 던져야 한다.")
    void t5() {
        Product product = createProduct(0);
        assertThrows(IllegalStateException.class, () -> productService.fetchOne(product.getId()));
    }

    private Product createProduct(int quantity) {
        Product product = Product.builder()
                .quantity(quantity)
                .restockVersion(0)
                .build();
        return productRepository.save(product);
    }
}