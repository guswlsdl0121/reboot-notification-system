package com.reboot_course.notification_system.common.cache;

import java.util.Map;

public interface Cache<K, V> {
    void set(K key, V value);

    V get(K key);

    void remove(K key);

    Map<K, V> getAll();
}