package com.reboot_course.notification_system.common.cache;

import java.util.Map;

public interface CacheRepository<T extends Cache<Long, Integer>> {
    void save(Long key, int value);

    int get(Long key);

    void delete(Long key);

    Map<Long, Integer> getAll();

    void syncWithDB();
}