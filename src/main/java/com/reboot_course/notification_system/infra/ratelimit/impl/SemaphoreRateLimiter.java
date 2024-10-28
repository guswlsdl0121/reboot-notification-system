package com.reboot_course.notification_system.infra.ratelimit.impl;

import com.reboot_course.notification_system.infra.ratelimit.RateLimiter;
import com.reboot_course.notification_system.infra.ratelimit.TaskProcessor;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/*
 * 세마포어를 사용하여 초당 처리량을 제한합니다.
 * 매 초마다 최대 500개의 세마포어를 생성하여 작업을 수행
 */
@Component
public class SemaphoreRateLimiter implements RateLimiter {
    private static final int RATE_LIMIT = 500;
    private static final int DELAY = 1;
    private static final int PERIOD = 1;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private final Semaphore semaphore = new Semaphore(RATE_LIMIT);
    private final ScheduledExecutorService scheduler;

    // 스케쥴러를 통해 1초마다 최대 500개의 세마포어를 보충함
    public SemaphoreRateLimiter() {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.scheduler.scheduleAtFixedRate(
                () -> semaphore.release(RATE_LIMIT - semaphore.availablePermits()),
                DELAY,
                PERIOD,
                TIME_UNIT
        );
    }

    @Override
    public void process(TaskProcessor taskProcessor) {
        Map<Long, List<Long>> targets = taskProcessor.getTargets();

        // 작업을 실행할 주체들을 순회하며 작업 실행
        for (Map.Entry<Long, List<Long>> entry : targets.entrySet()) {
            Long productId = entry.getKey();
            List<Long> userIds = entry.getValue();

            userIds.forEach(userId -> processTask(taskProcessor, productId, userId));
        }
    }

    // 본인의 순서가 온 작업은 세마포어를 획득하고 작업 시작
    private void processTask(TaskProcessor taskProcessor, Long productId, Long userId) {
        try {
            semaphore.acquire();
            taskProcessor.process(productId, userId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("처리가 중단되었습니다.", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();

        // 정상 종료 실패 시 강제 종료
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}