package com.reboot_course.notification_system.domain.notification.repository;

import com.reboot_course.notification_system.domain.notification.entity.NotificationHistory;
import org.springframework.data.repository.CrudRepository;

public interface NotificationHistoryRepository extends CrudRepository<NotificationHistory, Long> {
}
