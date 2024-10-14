package com.reboot_course.notification_system.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer restockVersion = 0;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public void updateRestockVersion() {
        ++restockVersion;
    }

    public boolean isOutOfStock() {
        return quantity < 1;
    }

    public boolean decreaseQuantity() {
        if (this.quantity < 1) {
            return false;
        }
        this.quantity--;
        return true;
    }
}