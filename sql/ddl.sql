create table product
(
    quantity        int         not null,
    restock_version int         not null,
    created_at      datetime(6) not null,
    id              bigint auto_increment primary key
);

create table product_notification_history
(
    restock_version   int                                                                            not null,
    created_at        datetime(6)                                                                    not null,
    id                bigint auto_increment
        primary key,
    last_send_user_id bigint                                                                         null,
    product_id        bigint                                                                         not null,
    status            enum ('CANCELED_BY_ERROR', 'CANCELED_BY_SOLD_OUT', 'COMPLETED', 'IN_PROGRESS') not null
);

create table product_user_notification
(
    is_active  bit         not null,
    created_at datetime(6) not null,
    id         bigint auto_increment
        primary key,
    product_id bigint      not null,
    updated_at datetime(6) not null,
    user_id    bigint      not null
);

create table product_user_notification_history
(
    restock_version int         not null,
    created_at      datetime(6) not null,
    id              bigint auto_increment
        primary key,
    user_id         bigint      not null
);