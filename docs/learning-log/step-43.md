# step-43: ApiV1MemberController 변환

- 강의 링크: https://www.slog.gg/p/14128#43강
- 상태: 완료

## 요구사항 요약

`ApiV1MemberController.java` 삭제 → `.kt` 변환. 회원가입/로그인/로그아웃/내정보 API. 중첩 `record` 요청/응답 바디 → `data class`(외부 record 스타일 호출 없음, R-013). 검증 어노테이션(`@NotBlank`, `@Size`)은 `@field:`로 명시(R-052 원칙을 Bean Validation에도 적용).

`.orElse(null) ?: throw ServiceException(...)` — 22강(R-035, `.orElseThrow { }`)의 대안 스타일. 둘 다 동일하게 동작.

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
