# step-14: MemberDto, MemberWithUsernameDto 변환

- 강의 링크: https://www.slog.gg/p/14128#14강
- 상태: 완료

## 요구사항 요약

`MemberDto.java`, `MemberWithUsernameDto.java`(둘 다 record) 삭제 → `data class`로 변환. 필드/순서는 원본 그대로 유지(강사 원본과 달리 이미 독립 record라 상속 이슈 없음, 12강과 동일 패턴).

`boolean isAdmin` 필드에 `@JsonProperty("isAdmin")`이 붙어있어, 코틀린 변환 시 `@get:JsonProperty("isAdmin")` use-site target을 명시해야 함.

강사 원본은 이 변환으로 필드 순서가 바뀌어 프론트엔드 `schema.d.ts`도 갱신했지만, 우리는 필드 순서/이름을 그대로 유지해 API 응답 구조가 안 바뀌므로 프론트엔드 스키마 변경 불필요.

`ApiV1MemberController`, `ApiV1AdmMemberController`의 사용처는 전부 생성자 호출뿐이라 영향 없음(grep 확인). `./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

### 질문1
- **Q.** `@get:JsonProperty("isAdmin") val isAdmin: Boolean` 부분 구체적으로 설명
- **A.** 코틀린 생성자 프로퍼티 `val isAdmin: Boolean` 하나는 컴파일 시 필드/생성자 파라미터/게터 메서드(`isAdmin()`) 세 가지로 나뉘어 생성됨. 어노테이션에 use-site target을 안 붙이면 기본적으로 생성자 파라미터에 붙는 경우가 많은데, Jackson은 직렬화 시 **게터 메서드**에 붙은 어노테이션만 인식하므로 파라미터에 붙은 어노테이션은 무시된다. `@get:`은 "이 어노테이션을 게터 메서드에 붙여라"는 use-site target 지정 문법. `@get:`이 없으면 Jackson이 `isAdmin()`을 boolean 게터로 인식해 관례대로 `is`를 뗀 `"admin"`을 JSON 키로 써버리는(의도와 다른) 문제가 생길 수 있음.
