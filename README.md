# basic-board

## Technology Stack
- **Backend**:
  - Language: Java 21
  - Framework: Spring Boot 3.5.3
  - Database: MySQL 8.0
  - ORM: Hibernate
  - Build Tool: Gradle

- **Frontend**:
  - Language: JavaScript
  - Package Manager: pnpm
  - Build Tool: Vite

### Requirements
**회원가입**
- 이메일 + 비밀번호 
- 이메일 중복 검증
- 비밀번호 암호화
- 닉네임 중복 검증
- 이메일/비밀번호 유효성 검사

**로그인/로그아웃**
- 이메일 + 비밀번호
- 세션 생성 및 관리
- 로그아웃 시 세션 무효화
- 로그인 실패 처리

**사용자 관리**
- 프로필 조회/수정
- 비밀번호 변경
- 회원 탈퇴
- 사용자 권한 관리(USER, ADMIN)

### Non Functional Requirements
**보안**
- 세션 하이재킹 방지
- CSRF(Cross-Site Request Forgery) 방지
- XSS(Cross-Site Scripting) 방지
- 세션 타임아웃 설정
- 비밀번호 정책(최소 8자, 대문자, 소문자, 숫자, 특수문자 포함)

**성능**(백엔드만)
- 로그인 API 응답 시간 200ms 이하
- 세션 만료 시간: 30분
- 동시 세션 제한: 사용자당 5개
