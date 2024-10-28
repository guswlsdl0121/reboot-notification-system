package com.reboot_course.notification_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NotificationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationSystemApplication.class, args);
    }

}
