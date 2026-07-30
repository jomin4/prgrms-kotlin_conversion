# step-60: AuthTokenServiceTest 변환

- 강의 링크: https://www.slog.gg/p/14128#60강
- 상태: 완료

## 요구사항 요약

`AuthTokenServiceTest.java`를 `.kt`로 변환. 테스트 전환의 첫 파일이라, 이후 파일들에 적용할 패턴(아래)을 여기서 확정했다.

## 변환 패턴

- `@Value` 필드 → `@Value("\${...}")` + `lateinit var`(문자열) / `var ... = 0`(Int). 코틀린 문자열 템플릿과 `${}`가 충돌하므로 `\$`로 이스케이프(R-055).
- `Map.of(...)` → `mapOf(... to ...)`(R-056).
- `assertThat(x).isNotNull()`/`.isNotBlank()`/`.isTrue()` 같은 AssertJ의 `isXxx()` 메서드는 코틀린에서 프로퍼티(`.isNotNull`, `.isNotBlank`, `.isTrue`)로 호출된다 — Java Bean 접근자 규칙(`isXxx()`/`getXxx()`)을 따르는 모든 Java 메서드는 코틀린이 자동으로 합성 프로퍼티로 노출하기 때문.
- 자바 원본은 `Claims`(맵의 상위 타입)를 그대로 `containsAllEntriesOf`로 검증했지만, 코틀린에서는 `payload.all { (key, value) -> parsedPayload[key] == value }` 형태로 표현(제네릭 매칭 이슈 회피, 강사의 실제 58강 이후 커밋과 동일 패턴).
- `MemberService.findByUsername`이 52~56강 Optional 제거로 `Member?`를 반환하게 됐으므로, 테스트에서 값이 반드시 있어야 하는 지점은 `getOrThrow()`로 확정.

## 검증

`./gradlew compileTestKotlin` 통과 확인(그룹 마지막에 전체 `test`까지 실행, step-66 참고).

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
