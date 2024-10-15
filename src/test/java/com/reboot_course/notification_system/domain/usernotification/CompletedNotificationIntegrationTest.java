package com.reboot_course.notification_system.domain.usernotification;

import com.reboot_course.notification_system.domain.history.entity.NotificationHistory;
import com.reboot_course.notification_system.domain.usernotification.entity.NotificationCompleted;
import com.reboot_course.notification_system.domain.usernotification.repository.cache.BatchQueueRepository;
import com.reboot_course.notification_system.domain.usernotification.repository.db.NotificationCompletedRepository;
import com.reboot_course.notification_system.domain.usernotification.service.CompletedNotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class CompletedNotificationIntegrationTest {

    @Autowired
    private CompletedNotificationService completedNotificationService;

    @Autowired
    private BatchQueueRepository batchQueueRepository;

    @Autowired
    private NotificationCompletedRepository notificationCompletedRepository;

    @Test
    @DisplayName("배치 큐에 쌓인 히스토리 내역을 DB에 올바르게 저장해야 한다.")
    void testBatchQueue() {
        // Given
        NotificationHistory history = NotificationHistory.builder()
                .restockVersion(1)
                .build();

        Long userId1 = 1L;
        Long userId2 = 2L;

        // When
        completedNotificationService.addCompletedNotification(history, userId1);
        completedNotificationService.addCompletedNotification(history, userId2);
        completedNotificationService.saveAll();
        batchQueueRepository.clear();

        // Then
        List<NotificationCompleted> savedNotifications = notificationCompletedRepository.findAll();
        assertEquals(2, savedNotifications.size());
        assertTrue(savedNotifications.stream().anyMatch(n -> n.getUserId().equals(userId1)));
        assertTrue(savedNotifications.stream().anyMatch(n -> n.getUserId().equals(userId2)));
        assertTrue(batchQueueRepository.isEmpty());
    }
}