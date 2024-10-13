-- 1~5까지의 Product 테이블에 더미 데이터 삽입
INSERT INTO product (created_at, quantity, restock_version)
VALUES (NOW(), 100, 1),
       (NOW(), 50, 2),
       (NOW(), 75, 1),
       (NOW(), 0, 3),
       (NOW(), 25, 1);

-- 1~5까지의 product_id에 대해서 알림 등록정보 삽입
INSERT INTO product_user_notification (created_at, user_id, product_id)
VALUES (NOW() - INTERVAL 5 DAY, 1, 1),
       (NOW() - INTERVAL 4 DAY, 2, 1),
       (NOW() - INTERVAL 3 DAY, 3, 1),
       (NOW() - INTERVAL 2 DAY, 4, 2),
       (NOW() - INTERVAL 1 DAY, 5, 2),
       (NOW(), 6, 3),
       (NOW() + INTERVAL 1 DAY, 7, 3),
       (NOW() + INTERVAL 2 DAY, 8, 4),
       (NOW() + INTERVAL 3 DAY, 9, 4),
       (NOW() + INTERVAL 4 DAY, 10, 5);

-- 동일한 사용자가 여러 제품에 대해 알림을 신청
INSERT INTO product_user_notification (created_at, user_id, product_id)
VALUES (NOW() - INTERVAL 1 DAY, 1, 2),
       (NOW(), 1, 3),
       (NOW() + INTERVAL 1 DAY, 2, 3),
       (NOW() + INTERVAL 2 DAY, 2, 4);