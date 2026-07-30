# step-35: Member 변환

- 강의 링크: https://www.slog.gg/p/14128#35강
- 상태: 완료

## 요구사항 요약

`Member.java` 삭제 → `back/src/main/kotlin/com/back/domain/member/member/entity/Member.kt` 변환. 주 생성자(6개 필드) + 보조 생성자 2개(3인자: SecurityUser 정보로 즉석 구성용, 4인자: 신규 가입용).

### 새로운 개념

- **`@field:Column(unique = true)`**: JPA 어노테이션은 프로퍼티가 아니라 필드에 있어야 인식되므로, 8강의 `-Xannotation-default-target=param-property` 옵션이 있어도 관계/컬럼 어노테이션은 명시적으로 `@field:`를 붙이는 게 안전(19강 R-030과 같은 원칙, JPA 버전).
- **`password: String? = null`, `profileImgUrl: String? = null`**: 소셜로그인으로 가입한 회원은 비밀번호/프로필사진이 없을 수 있어 nullable + 기본값.
- **`profileImgUrlOrDefault` 안의 `?.let { return it }`**: 커스텀 게터 안에서 "값이 있으면 그 값으로 즉시 반환, 없으면 아래 기본값으로 진행"하는 조기 반환 패턴.
- **`authoritiesAsStringList.map { SimpleGrantedAuthority(it) }`**: 자바 스트림 스타일(`.stream().map().toList()`) 대신 코틀린 `List`의 `.map { }` 확장 함수를 바로 사용(R-011 패턴 재적용, 강사 버전의 `.stream().map{}.toList()` 잔재를 코틀린답게 정리).

`BUILD SUCCESSFUL` 확인 — `Rq.kt`(`Member(it.id, it.username, it.nickname)`), `CustomAuthenticationFilter.kt`(3인자), `MemberService.java`(4인자, 아직 자바) 등 모든 생성자 호출부가 새 시그니처와 정확히 일치함을 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
