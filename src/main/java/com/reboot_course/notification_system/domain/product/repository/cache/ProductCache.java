package com.reboot_course.notification_system.domain.product.repository.cache;

import com.reboot_course.notification_system.common.cache.Cache;
import com.reboot_course.notification_system.domain.product.entity.Product;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProductCache implements Cache<Long, Product> {
    private final ConcurrentHashMap<Long, Product> cache = new ConcurrentHashMap<>();

    @Override
    public void set(Long key, Product value) {
        cache.put(key, value);
    }

    @Override
    public Product get(Long key) {
        return cache.get(key);
    }

    @Override
    public void remove(Long key) {
        cache.remove(key);
    }

    @Override
    public Map<Long, Product> getAll() {
        return new ConcurrentHashMap<>(cache);
    }

    public boolean decrease(Long key) {
        Product product = cache.get(key);
        if (product == null || product.getQuantity() <= 0) {
            return false;
        }

        product.decreaseQuantity();
        return true;
    }
}