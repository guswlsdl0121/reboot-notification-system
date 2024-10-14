package com.reboot_course.notification_system.domain.subscription.repository;

import com.reboot_course.notification_system.domain.subscription.entity.NotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationSubscriptionRepository extends JpaRepository<NotificationSubscription, Long> {
    List<NotificationSubscription> findAllByProductIdOrderByCreatedAtAsc(Long productId);
}
