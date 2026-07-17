# step-12: PostDto, PostWithContentDto 변환

- 강의 링크: https://www.slog.gg/p/14128#12강
- 상태: 완료

## 요구사항 요약

`PostDto.java`, `PostWithContentDto.java`(둘 다 Java record) 삭제 → 각각 `back/src/main/kotlin/com/back/domain/post/post/dto/`에 `data class`로 변환.

### 강사 원본과의 차이 (중요)

강사의 원본 프로젝트는 `PostWithContentDto extends PostDto`(상속) 구조였고, 13강에 가서야 상속을 제거한다. 반면 우리 베이스 프로젝트(`p-14184-1`)는 두 DTO가 **이미 독립된 record**(상속 없음, 필드도 각자 완전히 보유)로 되어 있었다. 즉 우리는 시작부터 13강의 최종 형태(상속 없는 구조)를 갖고 있었던 셈. 그래서 이번에 바로 두 개의 독립적인 `data class`로 변환하는 것으로 12강+13강을 사실상 동시에 처리함.

R-013(record → data class) 그대로 적용, 새로운 게터 이슈 없음(10~11강에서 `Post`/`BaseEntity` 게터 이미 구현).

`ApiV1PostController`의 사용처는 전부 `new PostDto(post)`/`new PostWithContentDto(post)` 생성자 호출뿐이라 영향 없음(grep으로 확인).

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음 (R-013 패턴 반복 적용)

## 질문 로그

(없음)
