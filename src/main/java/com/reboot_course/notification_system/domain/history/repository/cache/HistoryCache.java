package com.reboot_course.notification_system.domain.history.repository.cache;

import com.reboot_course.notification_system.domain.history.entity.NotificationHistory;
import com.reboot_course.notification_system.infra.cache.Cache;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class HistoryCache implements Cache<Long, NotificationHistory> {
    private final ConcurrentHashMap<Long, NotificationHistory> cache = new ConcurrentHashMap<>();

    @Override
    public void set(Long key, NotificationHistory value) {
        cache.put(key, value);
    }

    @Override
    public NotificationHistory get(Long key) {
        return cache.get(key);
    }

    @Override
    public void remove(Long key) {
        cache.remove(key);
    }

    @Override
    public Map<Long, NotificationHistory> getAll() {
        return new ConcurrentHashMap<>(cache);
    }
}