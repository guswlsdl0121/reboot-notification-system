DROP TABLE IF EXISTS product_user_notification_history;
DROP TABLE IF EXISTS product_notification_history;
DROP TABLE IF EXISTS product_user_notification;
DROP TABLE IF EXISTS product;


CREATE TABLE IF NOT EXISTS product
(
    id              BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at      DATETIME(6)           NOT NULL,
    quantity        INT                   NOT NULL,
    restock_version INT                   NOT NULL
);

CREATE TABLE IF NOT EXISTS product_notification_history
(
    id              BIGINT AUTO_INCREMENT                                                          NOT NULL PRIMARY KEY,
    created_at      DATETIME(6)                                                                    NOT NULL,
    restock_version INT                                                                            NOT NULL,
    status          ENUM ('CANCELED_BY_ERROR', 'CANCELED_BY_SOLD_OUT', 'COMPLETED', 'IN_PROGRESS') NOT NULL,
    product_id      BIGINT                                                                         NOT NULL,
    INDEX idx_pnh_pid (product_id)
);

CREATE TABLE IF NOT EXISTS product_user_notification
(
    id         BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at DATETIME(6)           NOT NULL,
    user_id    BIGINT                NOT NULL,
    product_id BIGINT                NOT NULL,
    UNIQUE KEY uk_pun_uid_pid (user_id, product_id),
    INDEX idx_pun_pid (product_id)
);

CREATE TABLE IF NOT EXISTS product_user_notification_history
(
    id                              BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at                      DATETIME(6)           NOT NULL,
    user_id                         BIGINT                NOT NULL,
    product_notification_history_id BIGINT                NOT NULL,
    INDEX idx_punh_pnhid (product_notification_history_id)
);