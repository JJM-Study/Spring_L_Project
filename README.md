
## 1. 프로젝트 개요
* **프로젝트명:** Digital E-Commerce
* ** 개발 인원 ** 1인 (Backend & Frontend)
* ** 주요 목표 **
  *  단순한 쇼핑몰 기능 구현을 넘어, **재고 정합성(Concurrency)**과 **데이터 무결성** 확보
  * 명확한 **에러 핸들링 규격** 정의를 통한 프론트엔드-백엔드 협업 효율 증대
  * **레거시 웹 환경(JSP)** 에서의 현대적인 보안(XSS, CSRF) 및 아키텍처 적용

---

## 2. Tech Stack

| 분류           | 기술 스택                                                |
| :----------- | ---------------------------------------------------- |
| **Backend**  | Java 17, Spring Boot 3.5.5, Spring Security, Mybatis |
| **Database** | MySQL 8.0 (Geometry Spatial),                        |
| **Frontend** | JSP, JSTL, JavaScript                                |
| **Testing**  | JUnit5, Mockito, Spring Test (MockMvc)               |
| **Tools**    | IntelliJ, Maven, GitHub                              |

---

## 3. System Architecture & Design (핵심 설계)

### 3-1. ERD 설계 및 최적화
> =**[ `Digital_E-Commerce.ppt`내 ERD 슬라이드 캡처 이미지 넣을 것 ]**

<details> 
<summary>전체 ERD (상세 스키마) 보기</summary>
![전체 ERD 다이어그램](https://raw.githubusercontent.com/JJM-Study/jjm/main/Repositoiry%20Resources/Digital%20E-Commerce/ERD/ERD_E-COMMERCE.png)
</details>


### 3-2. 패키지 구조 (Domain-Driven like)
> **=[ 여기에 PPT 3페이지 패키지 구조 이미지 포함할 것]**

* 기능별 응집도를 높이기 위해 도메인(Product, Order, Auth 등) 단위로 패키지를 구성.
* DTO 관리 전략 : 핵심 DTO는 독립 파일로, 보조 DTO는 Inner Class로 관리하여 클래스 폭발을 방지.

---

## 4. Key Technical Feature (기술적 도전과 해결)

### 1. 동시성 제어 (Concurrency Control)
*주문 폭주 상황에서 **재고 마이너스 (Over-selling)** 문제를 방지하기 위해 원자적 갱신 쿼리를 사용.

* **문제 :** 다수의 사용자가 동시에 주문 시 `Select-Update` 갭으로 인한 재고 불일치 발생 가능성.
* **검증 :** `CountDownLatch`를 활용하여 10개 스레드 동시 주문 테스트를 수행, 정확한 재고 차감을 검증.


|      주문 완료 결과 (UI)       | 동시성 테스트 로그 (Server Log) |
| :----------------------: | ----------------------- |
| <img src="" width ="400" | **[여기에 로그 캡처 이미지 삽입]**  |
|     *사용자에게 보여지는 결과*      | *실제 5건 성공, 5건 롤백된 검증*   |

### 2. 시스템 안정성 및 에러 핸들링
예측 가능한 예외 처리를 위해 **도메인별 비즈니스 에러 코드(Enum)** 를 정의하고 전역적으로 관리.

* ***구현 :** `GlobalExceptionHandler`를 통해 Runtime Exception을 잡고, 표준화 된 JSON 포맷으로 응답.

* **효과 :** 백엔드에서 구체적인 에러 코드(`S001`: 재고 부족, `A001`:인증 필요)를 정의하여 응답. 이를 통해, **프론트엔드에서 단순 메세지 출력이 아닌, 상황에 맞는 UX를 제공할 수 있음. 	
    *```
	ex)  if (response.code === 'A001') {
		// 에러 메세지 대신 로그인 페이지 이동
		redirectToLogin()
    }

** [=여기 PPT의 MockMvc 응답 JSON 또는 Postman 응답 JSON 이미지 넣을 것.]


### 3. 보안 (Security)
* **XSS 방어 :** `Jsoup` 라이브러리와 커스텀 `Servlet Filter`를 사용하여 악성 스크립트 유입을 원천 차단.
* **Spring Security** 인증/인가 처리 및 CSRF 토큰을 적용하여 웹 보안 취약점을 방어.

---

## 5. UI Preview (주요 화면)


|             메인 화면              | 상품 상세                          |
| :----------------------------: | ------------------------------ |
| <img src="3.png" width=100%">  | <img src="5.png" width=100%">  |
|        ** 로그인 / 회원가입 **        | ** 내 서재 (다운로드) **              |
| <img src="1.png" width="100%"> | <img src="9.png" width="100%"> |

---

### 6. Future Plan (확장 계획)

#### 1. GIS 기반 물류/배송 시스템 도입 (Physical Delivery)
* **목표 :** 디지털 상품을 넘어 실물 배송 프로세스 확장 
* ** 기술 : ** MySQL 8.0 Geometry 타입(Sptial Index)을 활용한 위치 기반 물류 시스템
* ** 설계 : ** `TB_DELI_MAST`(배송), `TB_DELI_GIS`(위치) 테이블 분리를 통해 주문 로직과의 간섭 최소화

#### 2. Role 기반 오픈 마켓 플랫폼 전환 (Multi-Vendor Platform)
* **목표 :** 관리자(Admin) 혼자 판매하는 쇼핑몰이 아닌, **일반 사용자도 권한 획득 후 판매자(Seller)가 될 수 있는 플랫폼** 으로 확장
* **설계 및 구현 계획 :**
    * **Spring Security 고도화:** `ROLE_USER` ->  `ROLE_SELLER`로의 동적 권한 승격 로직 구현
    * ** 데이터 격리 : ** 기존 `SELLER_ID` 컬럼을 활용하여, 판매자별로 자신의 상품과 주문 내역만 조회/관리할 수 있는 **판매자 전용 어드민 페이지(Dashboard)** 구축
    * **정산 시스템 :** 판매 수익금 및 정산 수수료 정책 로직 추가 예정

---

### 7. Troubleshooting

프로젝트 진행 중 발생한 기술적 이슈와 문제 해결 과정은 별도 문서로 정리 예정.


