package com.reboot_course.notification_system.domain.product.usecase;

import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.db.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductFinder {
    private final ProductRepository productRepository;

    @Transactional
    public Product fetchOneAndUpdateRestockCount(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(productId)));

        if (product.isOutOfStock()) {
            throw new IllegalStateException(String.format("현재 해당 상품의 재고가 없습니다. (id : %d)", productId));
        }

        product.updateRestockVersion();
        return product;
    }
}
