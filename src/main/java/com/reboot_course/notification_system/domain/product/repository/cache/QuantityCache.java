package com.reboot_course.notification_system.domain.product.repository.cache;

import com.reboot_course.notification_system.common.cache.Cache;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class QuantityCache implements Cache<Long, Integer> {
    private final ConcurrentHashMap<Long, AtomicInteger> cache = new ConcurrentHashMap<>();

    @Override
    public void set(Long key, Integer value) {
        cache.put(key, new AtomicInteger(value));
    }

    @Override
    public Integer get(Long key) {
        AtomicInteger quantity = cache.get(key);
        if (quantity == null) {
            return 0;
        }
        return quantity.get();
    }

    @Override
    public void remove(Long key) {
        cache.remove(key);
    }

    @Override
    public Map<Long, Integer> getAll() {
        Map<Long, Integer> result = new HashMap<>();
        cache.forEach((key, value) -> result.put(key, value.get()));
        return result;
    }

    public boolean decrease(Long key) {
        AtomicInteger quantity = cache.get(key);
        if (quantity == null) {
            return false;
        }

        int updatedQuantity = quantity.updateAndGet(current -> {
            if (current > 0) {
                return current - 1;
            }
            return current;
        });

        return updatedQuantity > 0;
    }
}