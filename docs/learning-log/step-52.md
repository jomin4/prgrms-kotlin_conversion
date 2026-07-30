# step-52: PostRepository::findFirstByOrderByIdDesc Optional 제거

- 강의 링크: https://www.slog.gg/p/14128#52강
- 상태: 완료

## 요구사항 요약

`PostRepository.findFirstByOrderByIdDesc(): Optional<Post>` → `Post?`(nullable). `PostService.findLatest()`도 동일하게 `Post?`로 변경. 자바 테스트(`ApiV1PostControllerTest.java`)의 `.findLatest().get()` → `.findLatest()`(더 이상 Optional이 아니므로 `.get()` 제거).

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
