# step-10: PostCommentDto 변환

- 강의 링크: https://www.slog.gg/p/14128#10강
- 상태: 완료
- 비고: 10강부터 사용자 승인 후 에이전트가 직접 파일에 반영하는 방식으로 전환

## 요구사항 요약

**Step 1**: `back/src/main/java/com/back/domain/post/postComment/dto/PostCommentDto.java`(Java record) 삭제 → `back/src/main/kotlin/com/back/domain/post/postComment/dto/PostCommentDto.kt`(Kotlin `data class`) 생성. 우리 프로젝트 베이스(`p-14184-1`)는 강사 원본과 달리 이미 record였으므로, 강사가 13강에서야 하는 "class → data class" 전환을 지금 바로 적용 (record의 자연스러운 코틀린 대응이 data class).

```kotlin
data class PostCommentDto(
    val id: Int,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val authorId: Int,
    val authorName: String,
    val postId: Int,
    val content: String
) {
    constructor(postComment: PostComment) : this(
        id = postComment.id,
        createDate = postComment.createDate,
        modifyDate = postComment.modifyDate,
        authorId = postComment.author.id,
        authorName = postComment.author.name,
        postId = postComment.post.id,
        content = postComment.content
    )
}
```

**Step 2**: `PostComment.java`, `BaseEntity.java`에서 `@Getter`(Lombok) 제거하고 게터 메서드 직접 구현.

- `PostComment`: `getAuthor()`, `getPost()`, `getContent()` 추가
- `BaseEntity`: `getId()`, `getCreateDate()`, `getModifyDate()` 추가

### 왜 필요한가

Kotlin 컴파일러(`compileKotlin`)는 Java 소스를 어노테이션 프로세싱(Lombok 처리) 이전 상태로 직접 분석한다. Lombok의 `@Getter`가 게터 메서드를 "만들어 붙이는" 시점은 `compileJava` 단계인데, Kotlin은 그보다 먼저 원본 자바 소스만 보고 끝내버려서 Lombok이 생성할 예정인 메서드를 인식하지 못한다 (`unresolved reference`). 그래서 Kotlin에서 참조해야 하는 자바 엔티티는 게터를 손으로 직접 구현해야 한다.

`Member.getName()`은 원래 Lombok이 아니라 손으로 쓴 메서드(`nickname` 필드를 다른 이름으로 반환)라 이번엔 안 건드림. `Member`/`Post` 자체의 Lombok 게터는 11강에서 다룰 예정.

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인. `PostCommentDto` 사용처(`ApiV1PostCommentController`)는 전부 `new PostCommentDto(postComment)` 생성자 호출뿐이라 record→data class 전환에 영향 없음을 grep으로 확인.

## 아키텍처 다이어그램

```mermaid
flowchart LR
    K["PostCommentDto.kt\n(data class)"] -->|".author.id, .content 등\n프로퍼티 접근" B["PostComment.java\n(@Getter 제거, 직접 구현)"]
    B -->|상속| E["BaseEntity.java\n(@Getter 제거, 직접 구현)"]
```

## 질문 로그

(없음)
