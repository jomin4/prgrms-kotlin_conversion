# step-08: Jackson, JPA가 코틀린을 지원하도록

- 강의 링크: https://www.slog.gg/p/14128#8강
- 상태: 완료
- 비고: 이번 강은 사용자가 직접 타이핑하지 않고 에이전트가 `back/build.gradle.kts`를 직접 반영함

## 요구사항 요약

강사의 실제 커밋(0008)을 재현. `back/build.gradle.kts`에 다음을 추가:

1. `kotlin("plugin.jpa") version "2.2.20"` 플러그인 추가
2. `implementation("tools.jackson.module:jackson-module-kotlin")`, `implementation("org.jetbrains.kotlin:kotlin-reflect")` 추가 (프로젝트가 Jackson 3.x(`tools.jackson.*`) 계열임을 `gradlew dependencies`로 확인 후 그에 맞는 좌표 사용)
3. `testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")` 추가 (지금 당장 필요 없어도 미리 준비)
4. `kotlin { compilerOptions { ... } }` 블록에 `freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")` 추가 (기존 6강에서 만든 jvmTarget 설정 블록에 병합)
5. `allOpen { annotation("jakarta.persistence.Entity"); annotation("jakarta.persistence.MappedSuperclass"); annotation("jakarta.persistence.Embeddable") }` 블록 추가

### 각 항목의 이유

- `kotlin("plugin.jpa")`: 7강의 `plugin.spring`과 같은 원리. Hibernate가 지연 로딩을 위해 `@Entity` 클래스의 프록시(상속 서브클래스)를 만들어야 하는데, 코틀린 클래스는 기본 `final`이라 프록시 생성이 막힘.
- `allOpen { ... }`: `kotlin("plugin.jpa")`가 내부적으로 이미 이 어노테이션들을 자동 open 처리하지만, 어떤 어노테이션이 open 대상인지 소스에 명시적으로 남겨 가독성/확장성을 확보하기 위해 직접 씀.
- `jackson-module-kotlin`: Jackson은 리플렉션 기반 직렬화/역직렬화를 하는데, 기본 자바 리플렉션은 코틀린의 `data class`, 생성자 기본값, null 안정성을 모름 — 이 모듈이 그 간극을 메움.
- `kotlin-reflect`: `jackson-module-kotlin`이 코틀린 리플렉션(`kotlin.reflect.*`)을 쓰기 위해 필요.
- `-Xjsr305=strict`: 자바 라이브러리의 JSR-305 nullable 어노테이션(`@Nullable`/`@NonNull`)을 코틀린 컴파일러가 엄격하게 null 안정성 검사에 반영하도록 함.
- `-Xannotation-default-target=param-property`: 코틀린 주 생성자 파라미터가 동시에 프로퍼티일 때, 검증 어노테이션(`@NotNull` 등)을 파라미터와 프로퍼티 양쪽에 자동으로 적용.
- `kotlin-test-junit5`: 지금은 코틀린 테스트가 없지만 60강대에 테스트를 코틀린으로 옮길 때를 미리 대비.

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인 (아직 실제 엔티티/DTO를 코틀린으로 옮기지 않아 육안상 변화는 없음, 9강 이후부터 이 설정들이 실제로 쓰이기 시작함).

## 아키텍처 다이어그램

해당 없음 (빌드 설정 변경, 구조 변경 아님)

## 질문 로그

(없음)
