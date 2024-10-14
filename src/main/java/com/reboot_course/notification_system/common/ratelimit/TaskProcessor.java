package com.reboot_course.notification_system.common.ratelimit;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class TaskProcessor {
    @Getter
    private final Map<Long, List<Long>> targets;
    private final BiConsumer<Long, Long> action;

    public TaskProcessor(Long productId, List<Long> userIds, BiConsumer<Long, Long> action) {
        this.targets = Map.of(productId, userIds);
        this.action = action;
    }

    public void process(Long productId, Long userId) {
        action.accept(productId, userId);
    }
}