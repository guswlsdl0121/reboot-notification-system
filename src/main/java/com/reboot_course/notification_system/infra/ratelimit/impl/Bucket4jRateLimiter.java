package com.reboot_course.notification_system.infra.ratelimit.impl;

import com.reboot_course.notification_system.infra.ratelimit.RateLimiter;
import com.reboot_course.notification_system.infra.ratelimit.TaskProcessor;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class Bucket4jRateLimiter implements RateLimiter {
    private static final int RATE = 500; // 초당 처리할 수 있는 최대 요청 수
    private final Bucket bucket;

    public Bucket4jRateLimiter() {
        Bandwidth limit = Bandwidth.classic(RATE, Refill.intervally(RATE, Duration.ofSeconds(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    public void process(TaskProcessor taskProcessor) {
        Map<Long, List<Long>> targets = taskProcessor.getTargets();
        long startTime = System.nanoTime();
        int processedTasks = 0;

        for (Map.Entry<Long, List<Long>> entry : targets.entrySet()) {
            Long productId = entry.getKey();
            List<Long> userIds = entry.getValue();

            for (Long userId : userIds) {
                bucket.asBlocking().consumeUninterruptibly(1);
                taskProcessor.process(productId, userId);
                processedTasks++;

                if (processedTasks % RATE == 0) {
                    long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
                    if (elapsedTime < 1000) {
                        try {
                            Thread.sleep(1000 - elapsedTime);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    startTime = System.nanoTime();
                }
            }
        }
    }
}