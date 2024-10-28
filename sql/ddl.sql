CREATE TABLE product
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity        INT         NOT NULL,
    restock_version INT         NOT NULL,
    created_at      DATETIME(6) NOT NULL,
    INDEX idx_product_restock_version (restock_version),
    INDEX idx_product_quantity (quantity)
);

CREATE TABLE product_notification_history
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id        BIGINT                                                                         NOT NULL,
    restock_version   INT                                                                            NOT NULL,
    status            ENUM ('CANCELED_BY_ERROR', 'CANCELED_BY_SOLD_OUT', 'COMPLETED', 'IN_PROGRESS') NOT NULL,
    last_send_user_id BIGINT                                                                         NULL,
    created_at        DATETIME(6)                                                                    NOT NULL,
    INDEX idx_notification_history_product_id (product_id),
    INDEX idx_notification_history_product_status (product_id, status)
);

CREATE TABLE product_user_notification
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT      NOT NULL,
    user_id    BIGINT      NOT NULL,
    is_active  BIT         NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    INDEX idx_subscriber_product_id_created_at (product_id, created_at),
    INDEX idx_subscriber_product_id_id (product_id, id)
);

CREATE TABLE product_user_notification_history
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT      NOT NULL,
    restock_version INT         NOT NULL,
    created_at      DATETIME(6) NOT NULL,
    INDEX idx_notification_completed_created_at (created_at)
);