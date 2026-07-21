# step-20: SecurityConfig 변환

- 강의 링크: https://www.slog.gg/p/14128#20강
- 상태: 완료

## 요구사항 요약

`SecurityConfig.java` 삭제 → `back/src/main/kotlin/com/back/global/security/SecurityConfig.kt` 변환. Spring Security의 자바 메서드 체이닝 스타일을 **코틀린 전용 Security DSL**(`http { ... }`)로 전환.

### 우리 프로젝트 고유 부분 (강사 원본과 다른 점, 그대로 유지)

- `.cors(cors -> cors.configurationSource(corsConfigurationSource()))` 연결 — 강사의 최신 리포엔 없지만 우리 베이스(`p-14184-1`)엔 있어서 유지.
- CORS 허용 메서드에 `"OPTIONS"` 포함 — 마찬가지로 우리 원본 그대로 유지.

### 새로운 개념

- **`operator fun invoke` + `http { }`**: `import org.springframework.security.config.annotation.web.invoke`가 제공하는 `HttpSecurity.invoke(configure: HttpSecurity.() -> Unit)` 확장 함수 덕분에, `http(...)` 대신 `http { ... }`(중괄호만으로 호출) 문법을 쓸 수 있음. 코틀린의 연산자 오버로딩 기능.
- **수신 객체 지정 람다(lambda with receiver)**: `HttpSecurity.() -> Unit` 타입의 블록 안에서는 `this`(=http)의 멤버를 지역 함수처럼 바로 호출 가능 — `authorizeHttpRequests { }`, `csrf { }` 등이 메서드 체이닝 없이 나란히 나열됨.
- **`authorize(패턴, 권한)` DSL**: `.requestMatchers(패턴).permitAll()`을 한 줄로 압축. 단, 여러 경로를 한 번에 받는 자바 오버로드와 달리 패턴을 하나만 받아서, 여러 경로는 줄을 나눠서 반복.
- **SAM 인터페이스 람다**: `AuthenticationEntryPoint { _, response, _ -> ... }`, `AccessDeniedHandler { _, response, _ -> ... }` — 메서드 하나짜리 함수형 인터페이스를 람다로 직접 생성. `_`는 안 쓰는 파라미터 표시.

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
