# step-38: 엔티티에서 불변하는 필드(author, post)에 var 대신 val 사용

- 강의 링크: https://www.slog.gg/p/14128#38강
- 상태: 완료 (34~37강 진행 시 이미 반영됨)

## 요구사항 요약

`Post.author`, `PostComment.author`/`PostComment.post`는 한 번 설정된 후 절대 재할당되지 않는 필드(작성자/소속 게시글은 안 바뀜)이므로, 34~37강에서 엔티티를 작성할 때부터 처음부터 `var`가 아니라 `val`로 선언함. 이 강은 그 원칙을 재확인.

`title`, `content`, `nickname`, `profileImgUrl`, `apiKey` 등 `modify()`/`modifyApiKey()`로 실제로 값이 바뀌는 필드는 `var` 유지.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
