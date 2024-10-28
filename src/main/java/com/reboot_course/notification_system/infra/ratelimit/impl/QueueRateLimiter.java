package com.reboot_course.notification_system.infra.ratelimit.impl;

import com.reboot_course.notification_system.infra.ratelimit.RateLimiter;
import com.reboot_course.notification_system.infra.ratelimit.TaskProcessor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

/*
 * 큐에 작업들을 넣고, 1초마다 최대 500개의 작업을 수행하도록 제어하는 리미터
 */
@Primary
@Component
public class QueueRateLimiter implements RateLimiter {
    private static final int MAX_REQUESTS_PER_SECOND = 500;
    private static final long WINDOW_SIZE_MS = 1000;

    private final Queue<Task> queue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger taskCount = new AtomicInteger(0);
    private final AtomicLong windowStart = new AtomicLong(System.currentTimeMillis());

    @Override
    public void process(TaskProcessor taskProcessor) {
        enqueueTasks(taskProcessor.getTargets());
        processQueue(taskProcessor::process);
    }

    private void enqueueTasks(Map<Long, List<Long>> targets) {
        targets.forEach((productId, userIds) ->
                userIds.forEach(userId -> queue.offer(new Task(productId, userId)))
        );
    }

    private void processQueue(BiConsumer<Long, Long> action) {
        while (!queue.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - windowStart.get();

            if (elapsedTime >= WINDOW_SIZE_MS) {
                windowStart.set(currentTime);
                taskCount.set(0);
            }

            if (taskCount.get() >= MAX_REQUESTS_PER_SECOND) {
                sleep(windowStart.get() + WINDOW_SIZE_MS - currentTime);
                continue;
            }

            Task task = queue.poll();
            if (task != null) {
                action.accept(task.productId, task.userId);
                taskCount.incrementAndGet();
            }
        }
    }

    private void sleep(long sleepTime) {
        try {
            Thread.sleep(Math.max(0, sleepTime));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void reset() {
        queue.clear();
        taskCount.set(0);
        windowStart.set(System.currentTimeMillis());
    }


    private record Task(Long productId, Long userId) {
    }
}