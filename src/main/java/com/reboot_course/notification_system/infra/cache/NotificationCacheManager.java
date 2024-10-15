package com.reboot_course.notification_system.infra.cache;

import com.reboot_course.notification_system.domain.history.repository.cache.HistoryCacheRepository;
import com.reboot_course.notification_system.domain.product.entity.Product;
import com.reboot_course.notification_system.domain.product.repository.cache.ProductCachedRepository;
import com.reboot_course.notification_system.domain.usernotification.repository.cache.BatchQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationCacheManager {
    private final ProductCachedRepository productCachedRepository;
    private final BatchQueueRepository batchQueueRepository;
    private final HistoryCacheRepository historyCacheRepository;

    public void initializeProcess(Product product) {
        productCachedRepository.startSyncScheduler(500);
        productCachedRepository.save(product.getId(), product);
        batchQueueRepository.clear();
    }

    public void finalizeProcess(Long productId) {
        productCachedRepository.stopSyncScheduler();
        productCachedRepository.delete(productId);
        historyCacheRepository.delete(productId);
        batchQueueRepository.clear();
    }
}