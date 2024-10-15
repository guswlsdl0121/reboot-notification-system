package com.reboot_course.notification_system.infra.cache;

import java.util.Map;

public interface Cache<K, V> {
    void set(K key, V value);

    V get(K key);

    void remove(K key);

    Map<K, V> getAll();

    void clear();
}