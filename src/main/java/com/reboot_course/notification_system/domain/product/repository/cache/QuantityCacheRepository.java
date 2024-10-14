package com.reboot_course.notification_system.domain.product.repository.cache;

import com.reboot_course.notification_system.common.cache.CacheRepository;
import com.reboot_course.notification_system.domain.product.repository.db.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class QuantityCacheRepository implements CacheRepository<QuantityCache> {
    private final QuantityCache cache;
    private final ProductRepository productRepository;

    @Autowired
    public QuantityCacheRepository(QuantityCache cache, ProductRepository productRepository) {
        this.cache = cache;
        this.productRepository = productRepository;
    }

    @Override
    public void save(Long productId, int quantity) {
        cache.set(productId, quantity);
    }

    @Override
    public int get(Long productId) {
        return cache.get(productId);
    }

    @Override
    public void delete(Long productId) {
        cache.remove(productId);
    }

    @Override
    public Map<Long, Integer> getAll() {
        return cache.getAll();
    }

    public boolean decrease(Long productId) {
        return cache.decrease(productId);
    }

    @Override
    @Scheduled(fixedRate = 100)
    @Transactional
    public void syncWithDB() {
        Map<Long, Integer> cachedQuantities = cache.getAll();
        List<Long> productIds = new ArrayList<>(cachedQuantities.keySet());

        //최신 상품 수량 가져오기
        List<Object[]> dbResults = productRepository.findQuantitiesByIds(productIds);
        
        //Map에 캐시에 값 삽입 
        for (Object[] result : dbResults) {
            Long productId = (Long) result[0];
            Integer dbQuantity = ((Number) result[1]).intValue();
            
            cache.set(productId, dbQuantity);
        }
    }
}