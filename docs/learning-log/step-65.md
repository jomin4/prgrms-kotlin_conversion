# step-65: ApiV1PostCommentControllerTest 변환

- 강의 링크: https://www.slog.gg/p/14128#65강
- 상태: 완료

## 요구사항 요약

`ApiV1PostCommentControllerTest.java`를 `.kt`로 변환. 마지막 컨트롤러 테스트.

## 변환 포인트

- `Post.findCommentById(id)`가 nullable(`PostComment?`)이므로 `.getOrThrow()`.
- `post.getComments().getLast()`(자바 21의 `SequencedCollection.getLast()`) → 코틀린 표준 라이브러리의 `List<T>.last()`.
- 자바 원본의 사용하지 않는 `MemberService memberService` 필드는 63강과 동일한 이유로 제거.
- 403 오류 메시지는 64강과 같은 이유로 `PostComment.checkActorCanModify`/`checkActorCanDelete`가 실제로 던지는 메시지(`"${id}번 댓글 수정 권한이 없습니다."`, 띄어쓰기 있음)를 그대로 사용(강사 원본의 띄어쓰기 없는 버전 대신 우리 코드 기준).

## 검증

`compileTestKotlin` 통과, 그리고 이 시점에 6개 테스트 파일 전환이 모두 끝났으므로 **전체 `./gradlew test`를 실행**해서 검증(step-66에서 실행 결과 기록).

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
