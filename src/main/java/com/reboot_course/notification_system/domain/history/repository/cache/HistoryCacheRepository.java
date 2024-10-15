package com.reboot_course.notification_system.domain.history.repository.cache;

import com.reboot_course.notification_system.domain.history.entity.NotificationHistory;
import com.reboot_course.notification_system.domain.history.repository.db.NotificationHistoryRepository;
import com.reboot_course.notification_system.infra.cache.Cache;
import com.reboot_course.notification_system.infra.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class HistoryCacheRepository implements CacheRepository<Long, NotificationHistory> {
    private final Cache<Long, NotificationHistory> cache;
    private final NotificationHistoryRepository dbRepository;

    @Override
    public void save(Long key, NotificationHistory value) {
        cache.set(key, value);
    }

    @Override
    public NotificationHistory get(Long key) {
        NotificationHistory history = cache.get(key);
        if (history == null) {
            history = dbRepository.findByProduct_Id(key).orElse(null);
            if (history != null) {
                cache.set(key, history);
            }
        }
        return history;
    }

    @Override
    public void delete(Long key) {
        cache.remove(key);
    }

    public NotificationHistory updateLastReceiver(Long productId, Long userId) {
        NotificationHistory history = get(productId);
        if (history != null) {
            history.updateLastReceiver(userId);
            save(productId, history);
        }
        return history;
    }

    public void clear(){
        cache.clear();
    }
}