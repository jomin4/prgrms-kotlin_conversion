# step-53: MemberRepository::findByUsername Optional 제거

- 강의 링크: https://www.slog.gg/p/14128#53강
- 상태: 완료

## 요구사항 요약

`MemberRepository.findByUsername(): Optional<Member>` → `Member?`. 연쇄적으로 영향받은 호출부:

- `MemberService.findByUsername`: 반환 타입 `Member?`로 변경
- `MemberService.join`: `.ifPresent { throw ... }` → `?.let { throw ... }`
- `MemberService.modifyOrJoin`: `var` 재할당 방식(`val existing = ...orElse(null); if (existing==null) {...}`) 대신 `findByUsername(username)?.let { 수정+RsData } ?: run { 가입+RsData }` 형태의 단일 표현식으로 재구성 — 조건 분기가 있어도 `var` 없이 표현 가능함을 보여주는 사례
- `CustomUserDetailsService.loadUserByUsername`: `.orElseThrow { UsernameNotFoundException(...) }` → `?: throw UsernameNotFoundException(...)`
- `ApiV1MemberController.login`: `.findByUsername(...).orElse(null) ?: throw ...` → `.findByUsername(...) ?: throw ...`(이미 nullable이라 `.orElse(null)` 불필요)
- `NotProdInitData.work2`: `.get()` → `.getOrThrow()`(51강에서 준비해둔 확장 함수 처음 사용)
- 자바 테스트 5개 파일의 `.findByUsername(...).get()` → `.findByUsername(...)`

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
