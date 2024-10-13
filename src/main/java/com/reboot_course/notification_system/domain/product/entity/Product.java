package com.reboot_course.notification_system.domain.product.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer restockVersion;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
}