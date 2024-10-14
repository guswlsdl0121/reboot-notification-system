package com.reboot_course.notification_system.domain.subscriber.repository;

import com.reboot_course.notification_system.domain.subscriber.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    List<Subscriber> findAllByProductIdOrderByCreatedAtAsc(Long productId);
}
