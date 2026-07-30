# step-54: MemberRepository::findByApiKey Optional 제거

- 강의 링크: https://www.slog.gg/p/14128#54강
- 상태: 완료

## 요구사항 요약

`MemberRepository.findByApiKey(): Optional<Member>` → `Member?`. `MemberService.findByApiKey`도 동일. `CustomAuthenticationFilter.resolveMember`의 `.findByApiKey(apiKey).orElse(null) ?: throw ...` → `.findByApiKey(apiKey) ?: throw ...`.

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
