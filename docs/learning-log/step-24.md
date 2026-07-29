# step-24: CustomOAuth2LoginSuccessHandler 변환

- 강의 링크: https://www.slog.gg/p/14128#24강
- 상태: 완료

## 요구사항 요약

`CustomOAuth2LoginSuccessHandler.java` 삭제 → `back/src/main/kotlin/com/back/global/security/CustomOAuth2LoginSuccessHandler.kt` 변환. 부수적으로 23강에서 추가했지만 실제로 안 쓰이던 `CustomOAuth2UserService.kt`의 `logger` 필드 제거(죽은 코드 정리).

### 새로운 개념

- **`runCatching { }.getOrNull()`**: try-catch를 표현식으로 다루는 코틀린 관용구. 블록 실행 결과를 `Result`로 감싸고, `.getOrNull()`로 성공 시 값/실패 시 null을 꺼냄.
- **`.substringBefore('#')`**: `str.split("#", 2)[0]`(쪼개서 첫 조각만 취함)을 대체하는 문자열 확장 함수, 의도가 이름으로 드러남.
- **체인식으로 재할당 없애기**: 자바는 `redirectUrl = "/"`로 선언 후 `if` 안에서 재할당했는데, 코틀린은 `?.let { } → runCatching → ?.substringBefore → ?.takeIf → ?:`를 이어붙여 하나의 표현식으로 `val redirectUrl`을 한 번에 계산.

### 반영된 안전장치 (자바 원본에 없던 것)

자바 원본은 `state` 파라미터의 Base64 디코딩이 실패하면 예외가 그대로 터져 로그인 성공 처리 자체가 실패했음. 코틀린 버전은 `runCatching`으로 디코딩 실패를 감싸 조용히 기본 리다이렉트(`"/"`)로 넘어가도록 더 견고해짐 — 기능 손실 없는 개선이라 그대로 반영.

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
