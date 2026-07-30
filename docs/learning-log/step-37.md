# step-37: PostComment 변환

- 강의 링크: https://www.slog.gg/p/14128#37강
- 상태: 완료

## 요구사항 요약

`PostComment.java` 삭제 → `back/src/main/kotlin/com/back/domain/post/postComment/entity/PostComment.kt` 변환. 우리 원본의 `@ManyToOne(fetch = LAZY) private Post post;`(지연 로딩 지정)를 그대로 `@field:ManyToOne(fetch = LAZY) val post: Post`로 유지.

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
