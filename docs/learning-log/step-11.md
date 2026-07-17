# step-11: Member, Post에서 @Getter 제거 후 게터 메서드 직접구현

- 강의 링크: https://www.slog.gg/p/14128#11강
- 상태: 완료

## 요구사항 요약

10강과 같은 문제(R-012)를 `Member`, `Post` 엔티티에 적용. 이후 나올 DTO들이 이 엔티티들을 폭넓게 참조하므로 이번엔 모든 필드에 게터를 직접 구현.

- `Member.java`: `@Getter` 제거, `getUsername()`, `getPassword()`, `getNickname()`, `getApiKey()`, `getProfileImgUrl()` 추가 (`getName()`은 원래부터 손으로 쓴 커스텀 메서드라 그대로 유지)
- `Post.java`: `@Getter` 제거, `getAuthor()`, `getTitle()`, `getContent()`, `getComments()` 추가

`BaseEntity`의 게터는 10강에서 이미 처리되어 있어서, `Member`/`Post` 내부에서 쓰는 `getId()` 호출(`checkActorCanModify` 등)은 이번에 안 건드려도 정상 동작.

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음 (R-012 패턴을 다른 엔티티에 반복 적용, 새로운 구조 아님)

## 질문 로그

(없음)
