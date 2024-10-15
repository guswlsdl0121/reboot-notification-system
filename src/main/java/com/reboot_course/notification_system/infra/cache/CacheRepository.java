package com.reboot_course.notification_system.infra.cache;

import java.util.Map;

public interface CacheRepository<K, V> {
    void save(K key, V value);

    V get(K key);

    void delete(K key);

    Map<K, V> getAll();

    void syncWithDB();
}