package com.reboot_course.notification_system.domain.history.repository.db;

import com.reboot_course.notification_system.domain.history.entity.NotificationHistory;
import com.reboot_course.notification_system.domain.history.entity.NotificationStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationHistoryRepository extends CrudRepository<NotificationHistory, Long> {
    Optional<NotificationHistory> findByProduct_Id(Long productId);

    Optional<NotificationHistory> findByProduct_IdAndNotificationStatusIn(Long productId, List<NotificationStatus> list);
}
