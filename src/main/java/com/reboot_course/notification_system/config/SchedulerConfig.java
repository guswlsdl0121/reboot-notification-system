package com.reboot_course.notification_system.config;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Getter
@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {

    private ScheduledTaskRegistrar taskRegistrar;
    private ScheduledFuture<?> scheduledTask;

    @Override
    public void configureTasks(@NonNull ScheduledTaskRegistrar taskRegistrar) {
        this.taskRegistrar = taskRegistrar;
    }

    public void startSyncScheduler(Runnable task, long duration) {
        if (scheduledTask == null || scheduledTask.isCancelled()) {
            scheduledTask = Objects.requireNonNull(taskRegistrar.getScheduler()).scheduleAtFixedRate(
                    task,
                    Duration.ofMillis(duration)
            );
        }
    }

    public void stopSyncScheduler() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
            scheduledTask = null;
        }
    }
}