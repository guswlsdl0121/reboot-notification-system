package com.reboot_course.notification_system.domain.usernotification.repository.db;

import com.reboot_course.notification_system.domain.usernotification.entity.NotificationCompleted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationCompletedRepository extends JpaRepository<NotificationCompleted, Long> {
}
