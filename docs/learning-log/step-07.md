# step-07: 스프링 관련 클래스는 기본적으로 open 이어야 한다

- 강의 링크: https://www.slog.gg/p/14128#7강
- 상태: 완료

## 요구사항 요약

강사의 실제 커밋 두 개(0007, 0007-2)를 그대로 재현.

**Step A (커밋 0007)** — 수동으로 `open` 키워드를 붙여서 해결

```kotlin
@SpringBootApplication
@EnableJpaAuditing
open class BackApplication
```

- 코틀린 클래스는 기본이 `final`. Spring은 `@Configuration`, `@Transactional`, `@Async`, `@Cacheable` 등에서 런타임에 CGLIB 프록시(원 클래스를 상속한 서브클래스)를 만들어 빈으로 등록하는 방식을 쓰는데, `final` 클래스는 상속이 안 돼서 프록시 생성이 막힘. `@SpringBootApplication`은 내부적으로 `@Configuration`을 포함하므로 이 클래스도 대상이 될 수 있음.

**Step B (커밋 0007-2)** — `kotlin("plugin.spring")` 플러그인으로 자동화, `open` 제거

```kotlin
// build.gradle.kts
plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.spring") version "2.2.20"
}
```

```kotlin
// BackApplication.kt — open 제거, 원래대로
@SpringBootApplication
@EnableJpaAuditing
class BackApplication
```

`kotlin("plugin.spring")`은 `@Component`/`@Service`/`@Repository`/`@Configuration` 등이 붙은 클래스를 컴파일 시점에 자동으로 open 처리해줘서, 매번 수동으로 `open`을 붙일 필요가 없어짐.

`./gradlew compileKotlin compileJava` `BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음 (플러그인 설정 변경, 구조 변경 아님)

## 질문 로그

(없음)
