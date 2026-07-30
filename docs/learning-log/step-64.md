# step-64: ApiV1PostControllerTest 변환

- 강의 링크: https://www.slog.gg/p/14128#64강
- 상태: 완료

## 요구사항 요약

`ApiV1PostControllerTest.java`를 `.kt`로 변환. 6개 파일 중 가장 크다(테스트 15개 — 글 작성/수정/삭제/조회 전 시나리오 + 인증 실패 케이스들).

## 변환 포인트

- `PostService.findLatest()`/`findById(id)`가 52~56강에서 nullable로 바뀌었으므로 값이 필요한 지점마다 `.getOrThrow()`.
- `post.getAuthor().getName()` → `post.author.name`. 여기서 `name`은 `Member` 엔티티의 계산 프로퍼티(`val name: String get() = nickname`)라 자바의 `getName()`과 동일하게 동작한다. **주의**: 강사의 실제 60강대 리포지토리는 `post.author.nickname`을 쓰는데, 이는 강사 쪽 `Member` 엔티티 필드명이 우리 프로젝트와 달라진 지점(강사 원본은 `nickname` 직접 노출)이라서다. 우리 프로젝트는 지금까지 `name`을 API 응답 필드로 써왔으므로(`PostDto.authorName = post.author.name`), 강사 커밋을 그대로 베끼지 않고 **우리 엔티티의 실제 필드**를 기준으로 변환했다.
- 마찬가지로 403 오류 메시지도 강사 원본은 `"%d번 글 수정권한이 없습니다."`(띄어쓰기 없음)이지만, 우리 `Post.checkActorCanModify()`가 실제로 던지는 메시지는 `"${id}번 글 수정 권한이 없습니다."`(띄어쓰기 있음)이므로 우리 코드 기준으로 맞췄다.
- `"%d번 글이 작성되었습니다.".formatted(post.getId())` → `"${post.id}번 글이 작성되었습니다."`(단순 삽입은 `.format()`보다 문자열 템플릿이 더 코틀린답다).
- `Integer.MAX_VALUE` → `Int.MAX_VALUE`.

## 검증

`compileTestKotlin` 통과(그룹 마지막에 전체 `test` 실행, step-66 참고).

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
