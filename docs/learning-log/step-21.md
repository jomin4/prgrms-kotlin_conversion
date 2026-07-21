# step-21: SecurityUser 변환

- 강의 링크: https://www.slog.gg/p/14128#21강
- 상태: 완료

## 요구사항 요약

`SecurityUser.java` 삭제 → `back/src/main/kotlin/com/back/global/security/SecurityUser.kt` 변환. (우리 프로젝트는 4개 파라미터 생성자(`id`, `username`, `nickname`, `authorities`) — 강사 원본은 `password`도 별도로 받지만 우리는 항상 빈 문자열 고정이라 그대로 유지.)

### 새로운 개념

- **주 생성자에서 바로 부모 생성자 호출 + 인터페이스 구현**: `class SecurityUser(...) : User(username, "", authorities), OAuth2User { ... }` — 자바처럼 본문 안에서 `super(...)`를 따로 호출하지 않고, 클래스 선언 한 줄에 상속+생성자 호출+인터페이스 구현을 전부 표현.
- **생성자 파라미터의 `val` 유무**: `val id`, `val nickname`은 프로퍼티가 되어 계속 접근 가능. `username`, `authorities`는 `val` 없이 그냥 부모 생성자에 전달만 하고 끝(부모가 이미 `getUsername()` 등을 제공하므로 중복 저장 불필요).
- **`Map.of()` → `emptyMap()`**: 코틀린 표준 라이브러리의 빈 맵 생성 함수.
- **`getUsername()` → `username`**: 부모(자바 클래스 `User`)의 게터도 9강의 synthetic property 규칙이 그대로 적용됨.
- **`Collection<? extends GrantedAuthority>` → `Collection<GrantedAuthority>`**: 코틀린의 `Collection<out E>`는 선언 시점에 이미 공변(covariant)으로 설계되어 있어서, 자바처럼 사용 지점마다 `? extends` 와일드카드를 반복할 필요가 없음.

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인 — `CustomAuthenticationFilter`, `CustomOAuth2UserService`, `CustomUserDetailsService`의 `new SecurityUser(...)` 호출부 무수정으로 정상 컴파일.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

### 질문1
- **Q.** `Collection<GrantedAuthority>`가 뭔지, `val`/`var` 표로 정리
- **A.** `GrantedAuthority`는 사용자 권한 하나(예: `ROLE_ADMIN`)를 표현하는 Spring Security 인터페이스, `Collection<GrantedAuthority>`는 권한 목록. `val`(재할당 불가, 자바 `final` 대응) vs `var`(재할당 가능) 표로 정리 — `val`도 참조만 불변이지 객체 내부까지 불변인 건 아님(예: `val list = mutableListOf(1); list.add(2)`는 가능).

### 질문2
- **Q.** `Collection<? extends GrantedAuthority>` → `Collection<GrantedAuthority>` 구체적으로 설명
- **A.** 자바 제네릭은 기본 불변이라 `List<Dog>`가 `List<Animal>`로 취급 안 됨(넣기 메서드가 있어서 안전성 문제). `? extends`는 "사용 지점"에서 예외적으로 읽기 전용 공변을 허용하는 와일드카드. 코틀린은 `Collection<out E>`처럼 인터페이스를 **정의하는 시점**에 아예 공변으로 선언해버려서(실제로 `add(E)` 같은 메서드가 `Collection`엔 없음, `MutableCollection`에만 있음), 사용할 때마다 와일드카드를 반복할 필요가 없음 — "선언 지점 가변성"이라 부름.
