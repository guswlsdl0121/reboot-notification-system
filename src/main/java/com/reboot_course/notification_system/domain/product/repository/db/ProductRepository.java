package com.reboot_course.notification_system.domain.product.repository.db;

import com.reboot_course.notification_system.domain.product.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    @Query("SELECT p.id, p.quantity FROM Product p WHERE p.id IN :ids")
    List<Object[]> findQuantitiesByIds(@Param("ids") Collection<Long> ids);
}
