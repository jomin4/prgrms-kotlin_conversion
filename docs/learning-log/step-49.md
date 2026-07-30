# step-49: 쓸데없는 타입선언 제거

- 강의 링크: https://www.slog.gg/p/14128#49강
- 상태: 완료

## 요구사항 요약

지금까지 변환하며 남아있던, 우변에서 이미 타입이 명확한데 굳이 명시한 타입 선언들을 코틀린의 타입 추론에 맡기도록 정리.

| 파일 | 변경 |
|---|---|
| `ApiV1PostController.kt`, `ApiV1PostCommentController.kt` | `val actor: Member = rq.actor` → `val actor = rq.actor` (43~46강 진행 중 이미 반영됨), 안 쓰는 `Member` import 제거 |
| `Member.kt` | `val authorities: MutableList<String> = ArrayList()` → `val authorities = mutableListOf<String>()` (자바식 `ArrayList()` 대신 코틀린 관용구) |
| `Post.kt` | `.filter { comment: PostComment -> ... }` → `.filter { comment -> ... }` (람다 파라미터 타입 추론) — 50강에서 이 메서드 자체를 더 손보므로 최종 형태는 50강 로그 참고 |
| `NotProdInitData.kt` | `.forEach { notProdMember: NotProdMember -> ... }` → `.forEach { notProdMember -> ... }`, 안 쓰는 `NotProdMember` import 제거 |
| `Ut.kt` | `val secretKey: Key = Keys.hmacShaKeyFor(...)` 패턴 — 확인 결과 애초에 명시적 타입 없이 작성돼 있어 수정 불필요 |

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
