package com.reboot_course.notification_system.infra.cache;

public interface CacheRepository<K, V> {
    void save(K key, V value);

    V get(K key);

    void delete(K key);
}