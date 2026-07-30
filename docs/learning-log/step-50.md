# step-50: 자바식 stream 문법 제거

- 강의 링크: https://www.slog.gg/p/14128#50강
- 상태: 완료

## 요구사항 요약

`Post.kt`의 `findCommentById`에 남아있던 자바 스타일 `.stream().filter{}.findFirst()`를 코틀린 컬렉션 함수로 교체(단, `Optional` 반환 자체는 56강에서 별도로 제거 예정이라 이번엔 유지).

```diff
 fun findCommentById(id: Int): Optional<PostComment> {
-    return comments
-        .stream()
-        .filter { comment -> comment.id == id }
-        .findFirst()
+    return Optional.ofNullable(comments.find { it.id == id })
 }
```

`comments.find { it.id == id }`는 코틀린 컬렉션의 네이티브 함수로, "조건에 맞는 첫 원소 또는 null"을 반환(18강 `firstOrNull`과 동일 계열, `find`가 그 별칭). `Optional.ofNullable(...)`로 감싸 지금 단계에서는 시그니처를 그대로 유지.

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
