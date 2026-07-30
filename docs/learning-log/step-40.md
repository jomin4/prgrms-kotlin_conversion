# step-40: AuthTokenService 변환

- 강의 링크: https://www.slog.gg/p/14128#40강
- 상태: 완료

## 요구사항 요약

`AuthTokenService.java` 삭제 → `back/src/main/kotlin/com/back/domain/member/member/service/AuthTokenService.kt` 변환.

### 새로운 개념

- **`@param:Value("\${...}")`**: 자바는 필드에 `@Value`를 붙였지만(필드 주입), 코틀린은 생성자 주입으로 바꾸면서(15강/17강 원칙) `@Value`를 **생성자 파라미터**에 붙여야 하므로 `@param:` use-site target 사용(14강/19강에서 배운 `@get:`/`@field:`에 이어 세 번째 target).
- **문자열 안의 `\$` 이스케이프**: 코틀린 문자열은 `$`를 문자열 템플릿(보간) 시작 기호로 쓰기 때문에, Spring의 `${...}` 플레이스홀더 문법을 그대로 문자열에 넣으려면 `\$`로 이스케이프해야 함(`"\${custom.jwt.secretKey}"`). 안 그러면 코틀린이 `${custom.jwt.secretKey}`를 변수 보간 표현식으로 잘못 해석하려고 시도.
- **`mapOf("k1" to v1, "k2" to v2)`**: 자바의 `Map.of(k1, v1, k2, v2)`(키-값을 번갈아 나열)를, 코틀린은 `to` 중위 함수로 `Pair`를 만들어 나열하는 `mapOf(...)`로 표현.
- **`parsedPayload["id"] as Int`**: `.get("id")` 대신 대괄호 표기(`[]`는 `get`의 연산자 문법).
- **필드 주입 → 생성자 주입 (`@Value`에도 적용)**: 15강/17강의 원칙(생성자 주입 선호)이 `@Autowired` 빈뿐 아니라 `@Value` 설정값에도 동일하게 적용됨.

`BUILD SUCCESSFUL` 확인. (참고: 코틀린은 자바의 package-private에 정확히 대응하는 접근제어자가 없어 `genAccessToken`/`payload`가 기본 `public`이 됨 — 이 프로젝트는 단일 Gradle 모듈이라 실질적 영향 없음.)

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
