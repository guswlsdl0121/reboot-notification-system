package com.reboot_course.notification_system.domain.product.usecase;

import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.ProductRepository;
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

        product.updateRestockVersion();

        return product;
    }
}
