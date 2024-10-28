package com.reboot_course.notification_system.domain.usernotification.service.ratelimit;

import com.reboot_course.notification_system.domain.product.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificationRateLimitTest extends AbstractRateLimitTest {
    @Test
    @Order(1)
    @DisplayName("한 개 요청 : 600명의 사용자에 대해 알림을 전송하려 한다면, 최소 1초 + @의 시간이 든다.")
    void t1() {
        Product product = createAndSaveProduct();
        createSubscriptions(product, 600);

        long duration = executeTest(() -> notificationSystemService.sendNotifications(product.getId()));

        assertDuration(duration, 1000);
    }

    /*
        각 테스트를 개별로 실행하면 오차없이 모두 통과하는데, 일괄 실행하면 300~400의 오차가 지속적으로 발생한다.
        시간 측정 문제거나, 테스트 간에 간섭이 생기는것 같은데, 어떻게 해결해야될 지 모르겠다.
    */
    @Test
    @Order(2)
    @DisplayName("네 개 요청 : product1에 300명, product2에 400명, product3에 400명, product4에 800명의 사용자에 대해 알림을 전송하려 한다면, 최소 3초 + @의 시간이 든다.")
    void t2() {
        Product product1 = createAndSaveProduct();
        Product product2 = createAndSaveProduct();
        Product product3 = createAndSaveProduct();
        Product product4 = createAndSaveProduct();

        createSubscriptions(product1, 300);
        createSubscriptions(product2, 400);
        createSubscriptions(product3, 400);
        createSubscriptions(product4, 800);

        long duration = executeTest(() -> {
            notificationSystemService.sendNotifications(product1.getId());
            notificationSystemService.sendNotifications(product2.getId());
            notificationSystemService.sendNotifications(product3.getId());
            notificationSystemService.sendNotifications(product4.getId());
        });

        assertDuration(duration, 3000, 800);
    }
}