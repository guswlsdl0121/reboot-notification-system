package com.reboot_course.notification_system.domain.product.repository;

import com.reboot_course.notification_system.domain.product.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
