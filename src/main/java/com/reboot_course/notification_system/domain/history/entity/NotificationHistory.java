package com.reboot_course.notification_system.domain.history.entity;

import com.reboot_course.notification_system.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_notification_history")
public class NotificationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer restockVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus notificationStatus;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private Long lastSendUserId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;

    public Long getProductId() {
        return this.product.getId();
    }

    public void updateLastReceiver(Long userId) {
        this.lastSendUserId = userId;
    }

    public boolean isSoldOut() {
        return this.notificationStatus == NotificationStatus.CANCELED_BY_SOLD_OUT;
    }

    public void determineStatus() {
        if (product.getQuantity() < 1) {
            this.notificationStatus = NotificationStatus.CANCELED_BY_SOLD_OUT;
            return;
        }

        this.notificationStatus = NotificationStatus.IN_PROGRESS;
    }

    public void completed() {
        this.notificationStatus = NotificationStatus.COMPLETED;
    }

    public void cancelByError() {
        this.notificationStatus = NotificationStatus.CANCELED_BY_ERROR;
    }

    public void cancelBySoldOut() {
        this.notificationStatus = NotificationStatus.CANCELED_BY_SOLD_OUT;
    }
}
