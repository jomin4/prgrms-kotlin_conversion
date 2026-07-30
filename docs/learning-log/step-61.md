# step-61: ApiV1AdmMemberControllerTest 변환

- 강의 링크: https://www.slog.gg/p/14128#61강
- 상태: 완료

## 요구사항 요약

`ApiV1AdmMemberControllerTest.java`를 `.kt`로 변환.

## 변환 포인트

- `MemberService.findById(id)`가 nullable로 바뀌었으므로 단건조회 테스트에서 `.getOrThrow()` 사용.
- `MockMvcResultMatchers.status().isOk()` 등도 `isXxx()` 자동 프로퍼티 규칙에 따라 `.isOk`로 호출(step-60 패턴 재사용).
- 문자열 결합(`"/api/v1/adm/members/" + id`) → 문자열 템플릿(`"/api/v1/adm/members/$id"`).
- `member.getCreateDate().toString().substring(0, 20)` → `member.createDate.toString().take(20)`.
- `for (int i = 0; ...)` 인덱스 루프 → `for (i in members.indices)`.

## 검증

`compileTestKotlin` 통과(그룹 마지막에 전체 `test` 실행, step-66 참고).

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
