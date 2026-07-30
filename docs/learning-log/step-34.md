# step-34: BaseEntity 변환

- 강의 링크: https://www.slog.gg/p/14128#34강
- 상태: 완료

## 요구사항 요약

`BaseEntity.java` 삭제 → `back/src/main/kotlin/com/back/global/jpa/entity/BaseEntity.kt` 변환. 모든 엔티티의 부모 클래스(id, createDate, modifyDate, equals/hashCode).

### 새로운 개념

- **`val id: Int = 0`을 주 생성자 파라미터로**: 자바는 `@Setter(PROTECTED)`로 `id`를 자식 클래스에서만 설정 가능하게 했는데, 코틀린은 `val id: Int = 0`을 `BaseEntity`의 주 생성자 파라미터로 받아서 자식이 `super(id)`로 넘기는 방식으로 통일. `id`는 생성 후 절대 안 바뀌므로 `val`(불변)이 적절.
- **JPA 엔티티에 필요한 no-arg 생성자는 어디로 갔나**: 자바는 `@NoArgsConstructor`(Lombok)로 JPA가 리플렉션으로 객체를 만들 때 필요한 기본 생성자를 만들어줬는데, 코틀린 버전엔 그런 게 안 보임. 8강에서 추가한 `kotlin("plugin.jpa")`가 내부적으로 "no-arg 컴파일러 플러그인"까지 포함하고 있어서, `@Entity`가 붙은 클래스에 자동으로 (보이지 않는) no-arg 생성자를 만들어줌 — 애플리케이션 코드에서는 절대 호출할 일 없고 JPA만 사용.
- **`lateinit var createDate/modifyDate`**: Spring Data JPA의 auditing 리스너(`@CreatedDate`/`@LastModifiedDate`)가 저장 시점에 나중에 채워 넣으므로 15강 패턴 그대로 적용.
- **`other === this`**: 코틀린의 `===`는 자바의 `==`(참조 동일성 비교)에 대응. 코틀린의 `==`는 `.equals()`를 호출하는 구조적 비교라서, "진짜 같은 객체인지"를 확인하려면 `===`를 써야 함.

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인 (BaseEntity를 상속하는 Member/Post/PostComment 및 이를 참조하는 모든 서비스/컨트롤러/테스트 포함).

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
