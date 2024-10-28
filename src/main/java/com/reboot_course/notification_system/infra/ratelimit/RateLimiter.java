package com.reboot_course.notification_system.infra.ratelimit;

@FunctionalInterface
public interface RateLimiter {
    void process(TaskProcessor taskProcessor);
}
