# step-62: ApiV1MemberControllerTest 변환

- 강의 링크: https://www.slog.gg/p/14128#62강
- 상태: 완료

## 요구사항 요약

`ApiV1MemberControllerTest.java`를 `.kt`로 변환. 6개 테스트 파일 중 가장 복잡한 편 — 쿠키(`Cookie`) 검증과 `MockMvcResultMatchers.andExpect(ResultMatcher)` 커스텀 람다 검증이 섞여 있다.

## 변환 포인트

- `andExpect(result -> { ... })`(자바 람다, `ResultMatcher` 함수형 인터페이스 구현) → `andExpect { result -> ... }`. `ResultMatcher`는 메서드 하나짜리 자바 함수형 인터페이스이므로 코틀린이 SAM(Single Abstract Method) 변환을 지원해 트레일링 람다 문법을 그대로 쓸 수 있다.
- `result.getResponse().getCookie("apiKey")`가 반환하는 `Cookie`는 null일 수 있는 자바 API라, `result.response.getCookie("apiKey")!!`로 non-null 단언(원본 자바 코드도 null 체크 없이 바로 `.getValue()`를 호출했으므로 동일한 "없으면 예외" 동작을 유지).
- `new Cookie("apiKey", actorApiKey)` → `Cookie("apiKey", actorApiKey)`(생성자 호출에 `new` 불필요).
- `"Bearer " + actorApiKey + " wrong-access-token"` → `"Bearer $actorApiKey wrong-access-token"`.
- `MemberService.findByUsername(...)`가 nullable이므로 `.getOrThrow()`로 확정 후 사용.

## 검증

`compileTestKotlin` 통과(그룹 마지막에 전체 `test` 실행, step-66 참고).

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
