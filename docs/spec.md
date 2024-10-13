# 재입고 알림 시스템 스펙

## 1. API 스펙

#### 1. 재입고 알림 전송 API
```http request
POST  /products/{productId}/notifications/re-stock
```
<br>

#### 2. 재입고 알림 전송 API (선택)
해당 API는 어떠한 이유로 알림 전송에 실패했을 때, 다시 보내는 API이다.
```http request
POST /admin/products/{productId}/notifications/re-stock
```

<br>
<br>

## 2. Table 설계
**Product**

- 상품
- 상품에 대한 데이터를 관리
- `재입고 회차`, `재고 수량` 등을 필수로 관리해야 한다.

<br>

**ProductNotificationHistory**

- 상품별 재입고 알림 히스토리
- 서버가 알림 전송 요청을 받으면, 이곳에 알림이 저장된다.
- 제품의 재입고 회차, 알림의 상태가 같이 관리되어야 한다.

<br>

**ProductUserNotificationHistory**

- 알림 전송(NotificationHistory)에 성공적으로 전달했으면, 이곳에 저장
- 이곳에 사용자id와 상품메시지id를 저장한다.

<br>

**ProducUserNotification**

- 상품별 재입고 알림을 설정한 유저 저장
- 사용자가 알림을 신청하는 동작을 따로 구현하진 않는다.
- 대신, 이곳에 어떤 상품에 대한 재입고 알림 신청을 한 사용자들이 있다고 가정한다.
