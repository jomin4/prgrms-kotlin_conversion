# step-44: ApiV1AdmMemberController 변환

- 강의 링크: https://www.slog.gg/p/14128#44강
- 상태: 완료

## 요구사항 요약

`ApiV1AdmMemberController.java` 삭제 → `.kt` 변환. 관리자용 회원 다건/단건 조회. `.stream().map(X::new).toList()` → `.map { X(it) }`(R-011 재적용) 외 새로운 개념 없음.

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
