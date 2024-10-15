# 재입고 알림 서비스

### 프로젝트 개요

이 프로젝트는 상품의 재입고 시 사용자에게 알림을 보내는 서비스이다.


### 주요 기능
- 재입고 알림 신청을 한 사용자들에게 알림 전송
- 전송 실패한 알림들에 대한 재전송 기능

### 기술 스택
- Java
- Spring Boot
- MySQL
- JPA
- Docker

### 실행 방법

1. Git clone
   ```
   git clone https://github.com/guswlsdl0121/reboot-notification-system
   ```
2. 디렉토리 이동

   ```
   cd [project directory]/docker
   ```

3. 도커 실행
   ``` 
   docker-compose up --build
   ```

### API 엔드포인트

1. 재입고 알림 전송 API
   ```
   POST /products/{productId}/notifications/re-stock
   ```

2. 재입고 알림 전송 API (수동 재전송)
   ```
   POST /admin/products/{productId}/notifications/re-stock
   ```

## 주요 구현 사항
- 캐시를 활용한 상품 정보 관리로 DB 부하 감소

- 배치 처리를 통한 효율적인 알림 전송

- 실패한 알림에 대한 재전송 메커니즘 구현

- RateLimiter를 통한 알림 전송 속도 제어