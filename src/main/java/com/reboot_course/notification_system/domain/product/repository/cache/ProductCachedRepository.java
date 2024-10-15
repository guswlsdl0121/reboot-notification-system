package com.reboot_course.notification_system.domain.product.repository.cache;

import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.db.ProductRepository;
import com.reboot_course.notification_system.infra.cache.Cache;
import com.reboot_course.notification_system.infra.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductCachedRepository implements CacheRepository<Long, Product> {
    private final Cache<Long, Product> cache;
    private final ProductRepository dbRepository;

    @Override
    public void save(Long key, Product product) {
        cache.set(key, product);
    }

    @Override
    public Product get(Long key) {
        Product product = cache.get(key);
        if (product == null) {
            product = dbRepository.findById(key).orElse(null);
            if (product != null) {
                save(key, product);
            }
        }
        return product;
    }

    @Override
    public void delete(Long key) {
        cache.remove(key);
    }


    @Scheduled(fixedRate = 100)
    @Transactional
    public void syncWithDB() {
        Map<Long, Product> cachedProducts = cache.getAll();
        List<Long> productIds = new ArrayList<>(cachedProducts.keySet());

        List<Product> dbProducts = dbRepository.findAllById(productIds);

        for (Product product : dbProducts) {
            cache.set(product.getId(), product);
        }
    }

    public void clear() {
        cache.clear();
    }
}