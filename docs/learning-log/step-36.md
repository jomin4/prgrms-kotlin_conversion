# step-36: Post 변환

- 강의 링크: https://www.slog.gg/p/14128#36강
- 상태: 완료

## 요구사항 요약

`Post.java` 삭제 → `back/src/main/kotlin/com/back/domain/post/post/entity/Post.kt` 변환.

### 확인 후 채택한 단순화

`deleteComment(PostComment postComment)`의 `if (postComment == null) return false;` null 체크를 제거하고 파라미터를 non-null로 변경. 실제 호출부(`ApiV1PostCommentController` → `PostService.deleteComment` → `Post.deleteComment`)를 추적한 결과, `postComment`는 항상 `post.findCommentById(id).get()`(Optional의 `.get()`, 비어있으면 이미 예외 발생)으로 얻은 값이라 이 시점에 null일 수 없음을 확인 — 죽은 방어 코드였으므로 제거.

`findCommentById`는 아직 자바 `Optional<PostComment>`를 그대로 반환(56강에서 Optional 제거 예정이라 지금은 유지).

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
