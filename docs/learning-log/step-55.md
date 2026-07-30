# step-55: MemberService::findById, PostService::findById Optional 제거

- 강의 링크: https://www.slog.gg/p/14128#55강
- 상태: 완료

## 요구사항 요약

`MemberService.findById`/`PostService.findById`를 `Member?`/`Post?`로 변경. **단, `MemberRepository`/`PostRepository`의 `findById`는 Spring Data JPA의 `JpaRepository` 표준 인터페이스가 이미 `Optional<T>`로 고정해둔 메서드라 직접 바꿀 수 없음** — 대신 서비스 계층에서 `.orElse(null)`로 변환:

```kotlin
fun findById(id: Int): Member? = memberRepository.findById(id).orElse(null)
```

Repository 계층은 JPA 표준을 따르고, Service 계층에서 코틀린다운 nullable로 변환하는 것이 적절한 경계. 51강의 `getOrThrow()` 확장 함수를 컨트롤러/`Rq.kt` 전반의 `.findById(...).get()` 자리에 `.getOrThrow()`로 교체(`ApiV1AdmMemberController`, `ApiV1PostController` 3곳, `ApiV1PostCommentController` 4곳, `Rq.actorFromDb`). 자바 테스트 3개 파일의 `.get()`도 제거.

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
