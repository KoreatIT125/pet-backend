# PetMediScan Backend

반려동물 안구/피부질환 진단 REST API 서버

## 기술 스택

- **Framework**: Spring Boot 2.7.18
- **Language**: Java 17
- **Database**: MySQL 8.0
- **Authentication**: JWT (JSON Web Token)
- **Build Tool**: Gradle
- **ORM**: JPA (Hibernate)
- **Validation**: Spring Validation
- **Documentation**: Swagger/OpenAPI

## 주요 기능

### 1. 사용자 관리
- 회원가입 (이메일 중복 검증)
- 로그인 (JWT 토큰 발급)
- 사용자 정보 조회/수정

### 2. 반려동물 관리
- 반려동물 등록
- 반려동물 목록 조회 (사용자별)
- 반려동물 정보 수정
- 반려동물 삭제

### 3. 진단 관리
- 안구 진단 요청 (AI 서버 연동)
- 피부 진단 요청 (AI 서버 연동)
- 진단 결과 저장
- 진단 이력 조회

### 4. 질환 정보 관리
- 질환 정보 데이터베이스
- 질환별 설명 및 대처법

## 프로젝트 구조

```
pet-backend/
├── src/main/java/com/petmediscan/
│   ├── config/               # 설정 클래스
│   │   ├── SecurityConfig.java
│   │   ├── JwtConfig.java
│   │   └── SwaggerConfig.java
│   ├── controller/           # REST API 컨트롤러
│   │   ├── AuthController.java
│   │   ├── PetController.java
│   │   └── DiagnosisController.java
│   ├── service/              # 비즈니스 로직
│   │   ├── AuthService.java
│   │   ├── PetService.java
│   │   ├── DiagnosisService.java
│   │   └── AiClientService.java
│   ├── repository/           # JPA Repository
│   │   ├── UserRepository.java
│   │   ├── PetRepository.java
│   │   └── DiagnosisRepository.java
│   ├── entity/               # JPA Entity
│   │   ├── User.java
│   │   ├── Pet.java
│   │   ├── Diagnosis.java
│   │   └── Disease.java
│   ├── dto/                  # Data Transfer Object
│   │   ├── request/
│   │   └── response/
│   └── exception/            # 예외 처리
│       ├── GlobalExceptionHandler.java
│       └── CustomException.java
└── src/main/resources/
    ├── application.yml
    └── data.sql              # 초기 데이터
```

## 데이터베이스 스키마

### users (사용자)
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### pets (반려동물)
```sql
CREATE TABLE pets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    species ENUM('DOG', 'CAT') NOT NULL,
    breed VARCHAR(100),
    birth_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

### diagnoses (진단 이력)
```sql
CREATE TABLE diagnoses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    type ENUM('EYE', 'SKIN') NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    result VARCHAR(100) NOT NULL,
    confidence DECIMAL(5,2) NOT NULL,
    diagnosis_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE
);
```

### diseases (질환 정보)
```sql
CREATE TABLE diseases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type ENUM('EYE', 'SKIN') NOT NULL,
    description TEXT,
    treatment TEXT
);
```

## API 명세

### Authentication

**POST** `/api/auth/register`
```json
{
  "email": "user@example.com",
  "password": "password123",
  "name": "홍길동"
}
```

**POST** `/api/auth/login`
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "userId": 1,
  "name": "홍길동"
}
```

### Pets

**GET** `/api/pets`
- Headers: `Authorization: Bearer {token}`
- Response: 반려동물 목록

**POST** `/api/pets`
```json
{
  "name": "몽이",
  "species": "DOG",
  "breed": "골든 리트리버",
  "birthDate": "2020-05-15"
}
```

### Diagnosis

**POST** `/api/diagnosis/eye`
- Content-Type: `multipart/form-data`
- FormData: `petId`, `image`

**POST** `/api/diagnosis/skin`
- Content-Type: `multipart/form-data`
- FormData: `petId`, `image`

**GET** `/api/diagnosis/history/:petId`
- Response: 진단 이력 리스트

## 환경 설정

### 1. application.yml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3308/petmediscan
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

jwt:
  secret: your-secret-key-here
  expiration: 86400000  # 24시간

ai:
  eye-service-url: http://localhost:5000
  skin-service-url: http://localhost:5001
```

### 2. 빌드 및 실행

```bash
# Gradle 빌드
./gradlew clean build

# 실행
java -jar build/libs/pet-backend-0.0.1-SNAPSHOT.jar

# 또는
./gradlew bootRun
```

### 3. Docker 실행

```bash
docker build -t petmediscan-backend .
docker run -p 8080:8080 petmediscan-backend
```

## Git Workflow

### Branch 전략
- `main`: 프로덕션
- `develop`: 개발
- `feature/backend-기능명`: 기능 개발

### Commit Convention
```
feat: 새로운 기능
fix: 버그 수정
docs: 문서
test: 테스트
refactor: 리팩토링
```

## 팀 구성원

- Backend 개발자 1: 인증, 사용자 관리
- Backend 개발자 2: 진단 API, AI 연동

## 참고 자료

- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Spring Security JWT](https://www.baeldung.com/spring-security-jwt)
- [JPA 가이드](https://spring.io/guides/gs/accessing-data-jpa/)
