# step-41: MemberService 변환

- 강의 링크: https://www.slog.gg/p/14128#41강
- 상태: 완료

## 요구사항 요약

`MemberService.java` 삭제 → `back/src/main/kotlin/com/back/domain/member/member/service/MemberService.kt` 변환. 회원가입/조회/비밀번호 검증/토큰 발급 위임 등. `.ifPresent { throw ... }`(람다), `if (!password.isNullOrBlank())`(nullable 문자열 검사, 17강 패턴) 정도만 새로 눈에 띄고 나머지는 기존 패턴 반복.

`BUILD SUCCESSFUL` 확인 — `AuthTokenService`/`MemberRepository` 생성자 주입, `Member`의 새 생성자 시그니처(35강)와 정확히 맞물림을 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
