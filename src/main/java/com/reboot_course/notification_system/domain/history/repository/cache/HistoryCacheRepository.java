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
            history = dbRepository.findByProductId(key).orElse(null);
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

    @Override
    public Map<Long, NotificationHistory> getAll() {
        return cache.getAll();
    }

    @Override
    public void syncWithDB() {
        Map<Long, NotificationHistory> allCachedHistories = cache.getAll();
        for (Map.Entry<Long, NotificationHistory> entry : allCachedHistories.entrySet()) {
            dbRepository.save(entry.getValue());
        }
        cache.getAll().clear();
    }
}