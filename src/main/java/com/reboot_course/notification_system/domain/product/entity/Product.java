package com.reboot_course.notification_system.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
}