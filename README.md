# 🏗️ 킵고잉로지 (Keep Going Logi)

> **MSA 기반 물류 관리 대규모 AI 시스템**

---

## 📘 1. 프로젝트 소개

### 💡 프로젝트 개요

**킵고잉로지(Keep Going Logi)** 는 기업 간 거래(B2B) 환경에서 **효율적인 허브 중심 물류 및 배송 관리**를 목표로 하는 시스템입니다.  
허브를 중심으로 재고를 체계적으로 관리하고, 주문 발생 시 **허브 간 이동 → 최종 배송**까지의 전 과정을 단계별로 처리할 수 있도록 설계되었습니다.

본 시스템은 **Spring Boot**와 **Spring Cloud (Eureka, Gateway, Config)** 를 기반으로 MSA 구조를 구현하여, 각 서비스가 독립적으로 **배포 및 확장**될 수 있도록 구성했습니다.

또한 **Java 21**, **Spring Boot 3.5.7**, **PostgreSQL**을 기반으로 개발되었으며, **JWT 인증**으로 서비스 간 통신의 **보안성**을 강화했습니다.  
**Dockerfile**을 활용해 컨테이너 단위로 빌드 및 배포함으로써, 환경에 관계없이 **일관된 실행 환경**을 유지할 수 있습니다.  
**Kafka**를 통해 **주문 이벤트를 발행 및 구독**하여 **실시간 알림 및 서비스 간 연동성**을 강화했습니다.

---

### 🎯 개발 목표

> “**완성된 결과물보다 과정 속 학습과 성장에 초점**”

우리 팀의 목표는 완성보다 **과정에서의 학습과 성장**에 있었습니다.  
MSA 기반의 물류 관리 시스템을 직접 설계하고 구현하며,  
**서비스 분리, 데이터 연동, 트랜잭션 처리, 협업 문제 해결**을 직접 경험하고 개선했습니다.

---

### 🧩 기술 스택

| 구분 | 기술 |
|------|------|
| **Backend** | Java 21, Spring Boot 3.5.7 (Eureka, Gateway, Config) |
| **Infra** | Docker |
| **Database** | PostgreSQL |
| **Messaging Queue** | Kafka |
| **Test** | Mockito, JUnit |
| **API Docs** | Swagger 2.8.14 |
| **Version Control** | Git |
| **Architecture & Methodology** | Domain-Driven Design (DDD) |

---

### 👥 담당 역할

| 이름 | 역할 | 담당 도메인 |
|------|------|-------------|
| **송의현** | 팀원 | 회원, 게이트웨이, Auth |
| **안지현** | 팀원 | 허브 |
| **류형선** | 팀원 | 배송 |
| **유영우** | 기술 리더 | 주문, 알림 |
| **윤혜지** | 팀장 | 업체, 상품 |

---

### 🏗️ 시스템 아키텍처
<img width="1251" height="1111" alt="keepgoing아키텍처이미지" src="https://github.com/user-attachments/assets/b13336fe-68ea-490e-8ef1-9033ec2233a7" />


### 🧱 ERD
<img width="1900" height="1217" alt="keepgoingerd" src="https://github.com/user-attachments/assets/0f9a2951-dafa-48d1-89f8-f1f24a537ddd" />


### 🗺️ Context Map
<img width="702" height="492" alt="keepgoingcontextmap" src="https://github.com/user-attachments/assets/cfdba90b-48c9-47be-948a-90f116a88fd4" />

---

## 🧩 2. 프로젝트 실행 방법

### 🌀 git clone
```
git clone https://github.com/FocusCrew-4/sparta_Logistics.git
```

### 🛠️ Project Build
```
gradle build --continue // 한 프로젝트에서 빌드가 실패하더라도, 다른 프로젝트의 빌드를 계속 진행
```

### 🐳 Docker 컨테이너 실행
```
docker compose up -d --build
```

## ⚙️ 3. 주요 기능

### 🔑 핵심 도메인별 API

📄 [API 명세서 바로가기](https://teamsparta.notion.site/API-29f2dc3ef51480a39ec0c91a6b00a036?source=copy_link)

#### 주요 기능 요약

- **QueryDSL** 기반 **동적 검색 기능**
- **Gateway 로컬 캐시 Logout 관리** → Redis 사용 대비 응답 속도 및 네트워크 I/O 비용 절감  
  (추후 Redis Pub/Sub으로 캐시 일관성 확보 예정)
- **허브 검색 기능**: Specification 기반의 **거리·소요시간·출발/도착 허브 동적 조합 검색**
- **주문 상태 기반 설계**
    - 상태 전이를 명확히 제어
    - DB 트랜잭션과 외부 API 호출을 분리하여 경계 명확화
    - **보상 트랜잭션 구현**으로 실패 시 상태 복원 가능
- **테스트 코드 적용**으로 시스템 안정성 확보
- **공통 응답 처리 구조화**로 일관된 사용자 경험 제공

---

## 🧭 4. 회고

### 🤝 협업 방식 및 Git 관리

- **매일 11시 데일리 스크럼 진행**
    - 전날 작업 내용 / 오늘 계획 공유
    - 진행 상황 점검 및 이슈 공유

- **소통 방식**
    - Slack + GitHub Issues로 작업 현황 공유
    - 커밋 메시지에 이슈 번호 포함
    - 최소 1인 이상 코드 리뷰 후 `develop` 브랜치 병합
    - `main` 브랜치는 **팀원 전원 참여 하에 병합**
    - Notion 대시보드로 **ERD, Context Map, API 명세, 트러블슈팅** 실시간 공유

#### 🪵 Branch 전략

| 브랜치 | 용도 |
|---------|------|
| `main` | 배포용 |
| `develop` | 통합 개발 브랜치 |
| `feature/*` | 기능 단위 개발 |
| `refactor/*` | 리팩토링 브랜치 |

---

### 🌟 잘 한 점

- 매일 스크럼을 통해 투명한 진행 공유 및 빠른 피드백
- 초기 설계에 충분히 집중 → 이후 개발 방향성 명확
- Context Map 기반으로 도메인 역할과 책임 명확화
- 동일한 개발 환경 세팅으로 협업 효율 극대화
- 실시간 페어프로그래밍을 통한 트러블슈팅 역량 향상
- 초기 일정 조율로 프로젝트 마감 시점에 **MVP 완성**

---

### ⚔️ 협업 중 발생한 문제와 해결

#### 💭 DDD 개념 이해 차이
- 팀원마다 다른 해석으로 인해 의사소통 혼선 발생  
  ➡️ **DDD 개념을 함께 학습하고 정의 통일**로 해결

#### 🔄 용어 충돌 문제
- 같은 용어를 다르게 해석하여 충돌 발생  
  ➡️ **Context Map 기반으로 역할 명확화**,  
  **언어 재정의 및 경계 구분**을 통해 해결

---
