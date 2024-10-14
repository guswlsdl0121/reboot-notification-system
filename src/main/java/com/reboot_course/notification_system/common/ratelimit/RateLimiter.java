package com.reboot_course.notification_system.common.ratelimit;

@FunctionalInterface
public interface RateLimiter {
    void process(TaskProcessor taskProcessor);
}
