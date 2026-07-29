# step-26: CustomAuthenticationFilter 변환

- 강의 링크: https://www.slog.gg/p/14128#26강
- 상태: 완료

## 요구사항 요약

`CustomAuthenticationFilter.java` 삭제 → `back/src/main/kotlin/com/back/global/security/CustomAuthenticationFilter.kt` 변환. 매 API 요청마다 실행되어 `Authorization` 헤더/쿠키의 apiKey·accessToken을 확인하고 `SecurityContextHolder`에 인증 정보를 등록하는 핵심 필터.

### 확인 후 결정한 사항

1. 강사 버전은 `logger.debug(...)` 3곳을 삭제했지만, 우리는 **로그를 유지**하기로 함.
2. 강사 버전은 `SecurityUser(id, username, "", name, authorities)`(5개, password 포함)를 쓰지만, 우리 `SecurityUser`(21강)는 4개 파라미터라 `SecurityUser(id, username, name, authorities)`로 조정.

### 새로운 개념

- **긴 메서드를 작은 private 함수로 분해 + `Pair` 반환**: `work()` 하나에 몰려있던 로직을 `isApiRequest`, `isPublicApi`, `extractTokens`, `resolveMember`, `memberFromAccessToken`, `refreshAccessToken`, `authenticate`로 분리. 서로 연관된 값 2개(`apiKey`+`accessToken`, `member`+`isAccessTokenValid`)는 `Pair`로 묶어 반환.
- **`in` 연산자로 컬렉션 포함 검사**: `list.contains(x)` → `x in list`.
- **`bits.getOrNull(1).orEmpty()`**: 배열 길이 체크 대신 `getOrNull`(범위 밖이면 예외 대신 null)+`orEmpty()`(17강/25강 패턴)로 안전하게 처리.
- **조기 반환 `?.let { return it to true }`**: 값이 있으면 그 자리에서 함수를 끝내는 패턴.

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음 (기존 로직의 함수 분해, 새 구조 아님)

## 질문 로그

(없음)
