# step-56: Post::findCommentById Optional 제거

- 강의 링크: https://www.slog.gg/p/14128#56강
- 상태: 완료

## 요구사항 요약

`Post.findCommentById(): Optional<PostComment>` → `PostComment?`(순수 엔티티 메서드라 JPA 표준 제약이 없어 바로 nullable로 변경 가능, `MemberRepository.findById` 같은 예외 없음).

```kotlin
fun findCommentById(id: Int): PostComment? {
    return comments.find { it.id == id }
}
```

`ApiV1PostCommentController`의 `.findCommentById(id).get()` 3곳 → `.getOrThrow()`. 자바 테스트(`ApiV1PostCommentControllerTest.java`)의 `.get()` 제거.

이로써 52~56강에 걸쳐 프로젝트 전역의 `Optional` 사용이 "JPA 표준이 강제하는 Repository의 `findById`" 한 곳만 남고 전부 제거됨(그마저도 Service 계층에서 즉시 nullable로 변환).

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
