# 자바 → 코틀린 전환 규칙 (팀 프로젝트 적용용)

이 문서는 `kotlin_conversion` 학습 프로젝트에서 **직접 확인하고 이해한 변환 패턴만** 규칙으로 뽑아 기록합니다.
각 규칙은 팀원의 자바 기반 프로젝트에 에이전트가 적용할 때 사용할 수 있도록, 다른 프로젝트에도 일반화 가능한 형태로 씁니다.

**적용 스코프 주의**: 여기 실린 규칙은 **아래 명시된 강(N강)까지 학습 완료된 것만** 포함합니다.
에이전트에게 이 문서를 넘길 때는 반드시 "이 문서에 있는 규칙만 적용하고, 문서에 없는 패턴은 임의로 적용하지 말 것"을 지시하세요.

- 현재 커버리지: **15강까지** (13강은 12강에서 선반영되어 규칙 추가 없음)
- 상세 학습 과정/트러블슈팅은 `docs/learning-log/step-NN.md` 참고 (규칙 하나당 어느 강에서 나왔는지 링크됨)

---

## 규칙 목록

| ID | 제목 | 카테고리 | 도입 강 |
|---|---|---|---|
| [R-001](#r-001-gradle-빌드에-kotlin-jvm-플러그인-추가) | Gradle 빌드에 Kotlin JVM 플러그인 추가 | build-config | 6강 |
| [R-002](#r-002-javakotlin-컴파일-jvm-타겟-정합성-맞추기) | Java/Kotlin 컴파일 JVM 타겟 정합성 맞추기 | build-config | 6강 |
| [R-003](#r-003-애플리케이션-진입점-main-변환) | 애플리케이션 진입점(`main`) 변환 | entrypoint | 6강 |
| [R-004](#r-004-spring-빈-클래스는-open이어야-함--kotlinplugin.spring) | Spring 빈 클래스는 `open`이어야 함 → `kotlin("plugin.spring")` | build-config | 7강 |
| [R-005](#r-005-jpa-엔티티-open-문제--kotlinplugin.jpa--allopen) | JPA 엔티티 `open` 문제 → `kotlin("plugin.jpa")` + `allOpen` | build-config | 8강 |
| [R-006](#r-006-jackson이-코틀린-클래스를-직렬화역직렬화하도록-지원) | Jackson이 코틀린 클래스를 직렬화/역직렬화하도록 지원 | build-config | 8강 |
| [R-007](#r-007-코틀린-컴파일러-옵션-null-안정성--어노테이션-타겟) | 코틀린 컴파일러 옵션 (null 안정성 / 어노테이션 타겟) | build-config | 8강 |
| [R-008](#r-008-체크-예외-처리-어노테이션-제거) | 체크 예외 처리 어노테이션 제거 | language-idiom | 9강 |
| [R-009](#r-009-자바-게터-메서드--코틀린-프로퍼티-접근) | 자바 게터 메서드 → 코틀린 프로퍼티 접근 | language-idiom | 9강 |
| [R-010](#r-010-문자열-포맷--멀티라인-문자열) | 문자열 포맷 & 멀티라인 문자열 | language-idiom | 9강 |
| [R-011](#r-011-java-stream--kotlin-컬렉션-함수) | Java Stream → Kotlin 컬렉션 함수 | language-idiom | 9강 |
| [R-012](#r-012-롬복-getter를-코틀린이-인식하지-못하는-문제) | 롬복 `@Getter`를 코틀린이 인식하지 못하는 문제 | interop | 10강 |
| [R-013](#r-013-java-record--kotlin-data-class) | Java `record` → Kotlin `data class` | entity-dto | 10강 |
| [R-014](#r-014-생성자-프로퍼티-어노테이션의-use-site-target-getjsonproperty) | 생성자 프로퍼티 어노테이션의 use-site target (`@get:JsonProperty` 등) | language-idiom | 14강 |
| [R-015](#r-015-static-필드메서드--companion-object) | `static` 필드/메서드 → `companion object` | language-idiom | 15강 |
| [R-016](#r-016-nullable--assertion--lateinit) | Nullable + `!!` 단언 → `lateinit` | language-idiom | 15강 |
| [R-017](#r-017-세터-주입--postconstruct--생성자-주입--init-블록) | 세터 주입 + `@PostConstruct` → 생성자 주입 + `init` 블록 | spring-di | 15강 |

---

## R-001: Gradle 빌드에 Kotlin JVM 플러그인 추가

- **카테고리**: build-config
- **도입 강**: [6강](../learning-log/step-06.md)
- **적용 조건**: 프로젝트에 `.kt` 파일을 하나라도 추가하기 전, 딱 한 번만 적용

### Before (자바 전용 `build.gradle.kts`)

```kotlin
plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
}
```

### After

```kotlin
plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.2.20"   // 프로젝트의 Spring Boot/JDK 버전에 맞는 최신 안정 버전 사용
}
```

### 주의사항

- `implementation(kotlin("stdlib-jdk8"))`를 별도로 추가하지 말 것 — `kotlin("jvm")` 플러그인이 `kotlin-stdlib`을 자동으로 의존성에 넣어준다. 스프링부트 3.x 이상에서는 수동 추가가 불필요하다.
- Spring 빈 클래스(`@Component`, `@Service` 등)를 코틀린으로 옮기기 시작하면 `kotlin("plugin.spring")` 플러그인도 필요해진다 (7강에서 다룰 예정, 아직 이 문서에 미포함).

---

## R-002: Java/Kotlin 컴파일 JVM 타겟 정합성 맞추기

- **카테고리**: build-config
- **도입 강**: [6강](../learning-log/step-06.md)
- **적용 조건**: `java { toolchain { languageVersion = ... } }`로 지정한 JDK 버전을 **현재 사용 중인 Kotlin 버전이 아직 지원하지 않을 때** (예: Kotlin이 지원하는 최대 JVM 타겟보다 프로젝트의 JDK 툴체인이 더 높은 경우)

### 증상

```
Inconsistent JVM Target Compatibility Between Java and Kotlin Tasks
  Inconsistent JVM-target compatibility detected for tasks 'compileJava' (25) and 'compileKotlin' (24).
```

### 해결 패턴

```kotlin
tasks.withType<JavaCompile> {
    options.release.set(24)   // Kotlin이 지원하는 최대 타겟에 맞춤
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24)
    }
}
```

### 주의사항

- 숫자(`24`)는 프로젝트 상황에 따라 다르다 — 반드시 빌드 에러 메시지에 찍히는 "Kotlin이 폴백한 버전"을 그대로 사용할 것. 임의로 낮추거나 높이지 말 것.
- JDK 툴체인 자체(빌드에 쓰는 JDK)는 그대로 최신 버전을 유지해도 된다. 바뀌는 건 "산출되는 바이트코드의 타겟 버전"뿐이다.

---

## R-003: 애플리케이션 진입점(`main`) 변환

- **카테고리**: entrypoint
- **도입 강**: [6강](../learning-log/step-06.md)
- **적용 조건**: `@SpringBootApplication`이 붙은 진입점 클래스 (프로젝트에 보통 1개)

### Before (Java)

```java
package com.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BackApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackApplication.class, args);
    }

}
```

### After (Kotlin)

```kotlin
package com.back

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class BackApplication

fun main(args: Array<String>) {
    runApplication<BackApplication>(*args)
}
```

### 변환 패턴 요약

1. `public class` → `class` (public 기본값이라 생략)
2. 클래스 안의 `static void main` → **클래스 밖** top-level `fun main(args: Array<String>)`
3. `SpringApplication.run(X.class, args)` → `runApplication<X>(*args)` (Spring Boot Kotlin 확장 함수, reified 제네릭 + 스프레드 연산자 `*` 사용)
4. 클래스 안에 필드/추가 메서드가 없으면 `class BackApplication`처럼 본문 `{}`를 생략 가능

### 주의사항

- import 경로가 자바와 다름: `org.springframework.boot.runApplication` (Spring Boot Kotlin 확장 함수)를 새로 import해야 함
- 파일/클래스명은 자바와 동일하게 유지 (`BackApplication`)

---

## R-004: Spring 빈 클래스는 `open`이어야 함 → `kotlin("plugin.spring")`

- **카테고리**: build-config
- **도입 강**: [7강](../learning-log/step-07.md)
- **적용 조건**: `@Component`, `@Service`, `@Repository`, `@Configuration`(및 이를 포함하는 `@SpringBootApplication`, `@RestController` 등)이 붙은 클래스를 코틀린으로 옮길 때

### 문제 상황

코틀린 클래스는 기본이 `final`이다. Spring은 `@Transactional`, `@Async`, `@Cacheable`, `@Configuration` 등을 구현할 때 런타임에 원 클래스를 상속한 CGLIB 프록시를 만들어 빈으로 등록하는 방식을 쓰는데, `final` 클래스는 상속이 불가능해 프록시 생성이 막힌다.

### 해결 패턴

```kotlin
// build.gradle.kts
plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.spring") version "2.2.20"   // 추가
}
```

`kotlin("plugin.spring")`을 추가하면 `@Component`/`@Service`/`@Repository`/`@Configuration` 등이 붙은 클래스를 컴파일 시점에 **자동으로 open 처리**해준다. 클래스마다 수동으로 `open class ...`를 붙일 필요가 없다.

### 주의사항

- 이 플러그인이 없을 때 임시방편으로 클래스에 직접 `open` 키워드를 붙여서 해결할 수도 있지만, 스프링 빈 클래스가 많아질수록 실수하기 쉬우므로 플러그인 적용이 정석이다.
- Kotlin JVM 플러그인(R-001)이 먼저 적용되어 있어야 `kotlin("plugin.spring")`도 의미가 있다 (같은 Kotlin 버전으로 맞출 것).

---

## R-005: JPA 엔티티 `open` 문제 → `kotlin("plugin.jpa")` + `allOpen`

- **카테고리**: build-config
- **도입 강**: [8강](../learning-log/step-08.md)
- **적용 조건**: `@Entity`, `@MappedSuperclass`, `@Embeddable`이 붙은 클래스를 코틀린으로 옮길 때

### 문제 상황

R-004와 같은 원리. Hibernate는 지연 로딩(lazy loading)을 위해 `@Entity` 클래스를 상속한 프록시를 런타임에 생성하는데, 코틀린 클래스는 기본 `final`이라 상속(프록시 생성)이 막힌다.

### 해결 패턴

```kotlin
// build.gradle.kts — plugins 블록
plugins {
    kotlin("plugin.jpa") version "2.2.20"
}

// 파일 하단
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}
```

### 주의사항

- `kotlin("plugin.jpa")`만 추가해도 위 세 어노테이션은 내부적으로 자동 open 처리된다. `allOpen {}` 블록을 굳이 명시하는 이유는 "어떤 어노테이션이 open 대상인지"를 소스에서 바로 보이게 하고, 필요 시 대상을 쉽게 추가/변경할 수 있게 하기 위함이다 (선택이지만 권장).
- Jakarta EE 패키지(`jakarta.persistence.*`)를 쓰는 프로젝트 기준. `javax.persistence.*`를 쓰는 구버전 프로젝트라면 패키지 경로를 그에 맞게 바꿔야 한다.

---

## R-006: Jackson이 코틀린 클래스를 직렬화/역직렬화하도록 지원

- **카테고리**: build-config
- **도입 강**: [8강](../learning-log/step-08.md)
- **적용 조건**: DTO/엔티티 등 JSON으로 변환되는 클래스를 코틀린으로 옮길 때 (REST API 프로젝트라면 사실상 필수)

### 문제 상황

Jackson은 리플렉션 기반으로 객체를 JSON으로 직렬화/역직렬화하는데, 기본 자바 리플렉션 API는 코틀린의 `data class`, 생성자 기본값(default parameter), null 안정성(nullable 타입) 같은 코틀린 고유 개념을 인식하지 못한다.

### 해결 패턴

```kotlin
dependencies {
    implementation("tools.jackson.module:jackson-module-kotlin")   // Jackson 3.x 계열
    // 구버전(Jackson 2.x) 프로젝트라면: implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")          // 코틀린 리플렉션, jackson-module-kotlin이 내부적으로 필요로 함
}
```

### 주의사항

- 프로젝트가 Jackson 2.x인지 3.x인지에 따라 `jackson-module-kotlin`의 groupId(`com.fasterxml.jackson.module` vs `tools.jackson.module`)가 다르다. `./gradlew dependencies --configuration compileClasspath | grep -i jackson`으로 실제 사용 중인 Jackson 계열을 먼저 확인할 것.
- `kotlin-reflect`는 크기가 있는 라이브러리라 꼭 필요한 경우에만(Jackson 코틀린 모듈처럼 리플렉션이 필요한 경우) 추가한다.

---

## R-007: 코틀린 컴파일러 옵션 (null 안정성 / 어노테이션 타겟)

- **카테고리**: build-config
- **도입 강**: [8강](../learning-log/step-08.md)
- **적용 조건**: Spring/자바 라이브러리의 `@Nullable`/`@NonNull`(JSR-305)을 쓰는 프로젝트, 또는 주 생성자 파라미터에 검증 어노테이션(`@NotNull` 등)을 붙이는 경우. 상황에 따라 필요 없을 수도 있는 선택적 규칙.

### 해결 패턴

```kotlin
kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}
```

- `-Xjsr305=strict`: 자바 라이브러리의 JSR-305 nullable 어노테이션을 코틀린 컴파일러가 엄격한 null 안정성 검사에 반영하도록 함.
- `-Xannotation-default-target=param-property`: 주 생성자 파라미터가 동시에 프로퍼티일 때, 어노테이션을 파라미터와 프로퍼티 양쪽에 자동 적용 (최신 Kotlin 버전에서 지정 안 하면 경고 발생).

### 주의사항

- 프로젝트에 이미 다른 `kotlin { compilerOptions { ... } }` 블록이 있다면(예: JVM 타겟 설정, R-002) **새 블록을 또 만들지 말고 기존 블록에 병합**할 것. 여러 개를 만들어도 동작은 하지만 설정이 흩어져 가독성이 떨어진다.

---

## R-008: 체크 예외 처리 어노테이션 제거

- **카테고리**: language-idiom
- **도입 강**: [9강](../learning-log/step-09.md)
- **적용 조건**: 자바 코드에서 체크 예외(checked exception)를 던지는 메서드를 호출하기 위해 `@SneakyThrows`(Lombok), `throws` 선언, try-catch로 감싼 부분

### 변환

```diff
-    @SneakyThrows
-    public String main() {
-        InetAddress localHost = getLocalHost();
+    fun main(): String {
+        val localHost = InetAddress.getLocalHost()
```

코틀린은 체크 예외 개념 자체가 없다 — 모든 예외가 자바의 `RuntimeException`(언체크 예외)처럼 취급된다. 그래서 체크 예외를 던지는 자바 메서드(`InetAddress.getLocalHost()`가 `UnknownHostException`을 던지는 것 등)를 코틀린에서 호출할 때는 `@SneakyThrows`, `throws` 선언, try-catch가 전부 불필요하다.

### 주의사항

- 예외를 실제로 처리해야 하는 비즈니스 로직(예: 실패 시 기본값 반환)이라면 여전히 try-catch를 쓴다. 이 규칙은 "체크 예외라서 억지로 감싸야 했던" 보일러플레이트만 제거 대상이다.

---

## R-009: 자바 게터 메서드 → 코틀린 프로퍼티 접근

- **카테고리**: language-idiom
- **도입 강**: [9강](../learning-log/step-09.md)
- **적용 조건**: 자바 클래스(코틀린으로 변환되지 않은 클래스 포함)의 `getXxx()`/`isXxx()` 게터를 코틀린 코드에서 호출할 때

### 변환

```diff
-<p>Host Name: ${localHost.getHostName()}</p>
-<p>Host Address: ${localHost.getHostAddress()}</p>
+<p>Host Name: ${localHost.hostName}</p>
+<p>Host Address: ${localHost.hostAddress}</p>
```

코틀린은 JavaBean 게터 규칙(`getXxx()`/`isXxx()`)을 따르는 자바 메서드를 발견하면, 코틀린 쪽에서는 `.xxx` 프로퍼티처럼 접근할 수 있게 자동 매핑해준다(synthetic property). 대상 클래스가 자바 클래스여도 적용된다(코틀린으로 변환할 필요 없음).

### 주의사항

- `setXxx(value)` 세터가 있는 경우 `.xxx = value` 형태의 대입도 가능하다.
- 파라미터가 있는 게터(`getXxx(int index)` 등)는 프로퍼티로 매핑되지 않는다.

---

## R-010: 문자열 포맷 & 멀티라인 문자열

- **카테고리**: language-idiom
- **도입 강**: [9강](../learning-log/step-09.md)
- **적용 조건**: 자바의 `String.format(...)`/`"...".formatted(...)` 또는 텍스트 블록(`"""..."""`)을 코틀린으로 옮길 때

### 변환

```diff
-return """
-        <h1>API 서버</h1>
-        <p>Host Name: %s</p>
-        """.formatted(localHost.getHostName());
+return """
+    |<h1>API 서버</h1>
+    |<p>Host Name: ${localHost.hostName}</p>
+""".trimMargin()
```

- `String.format`/`.formatted()` → 코틀린 **문자열 템플릿** `${표현식}`을 문자열 안에 직접 씀 (자리표시자+인자 나열 방식 자체가 불필요).
- 자바 text block은 공통 들여쓰기를 **자동으로** 제거하지만, 코틀린 `"""..."""`는 자동으로 제거되지 않는다. `.trimIndent()`(가장 적게 들여쓰기된 줄 기준 자동 제거) 또는 `.trimMargin()`(각 줄 앞 `|` 마커로 명시적 지정, 기본 마커 변경 가능)을 명시적으로 호출해야 한다.

### 주의사항

- 코드 들여쓰기가 여러 단계로 중첩된 곳에 있는 문자열이면 `trimIndent()`보다 `trimMargin()`이 더 예측 가능하다.

---

## R-011: Java Stream → Kotlin 컬렉션 함수

- **카테고리**: language-idiom
- **도입 강**: [9강](../learning-log/step-09.md)
- **적용 조건**: `.stream().collect(Collectors.xxx(...))` 패턴을 코틀린으로 옮길 때 (흔한 패턴일수록 대응되는 코틀린 표준 함수가 있음)

### 변환 예시 (Enumeration → Map)

```diff
-return Collections.list(session.getAttributeNames()).stream()
-        .collect(Collectors.toMap(
-                name -> name,
-                session::getAttribute
-        ));
+return session.attributeNames
+    .asSequence()
+    .associateWith { name -> session.getAttribute(name) }
```

자바의 `Collectors.toMap(keyMapper, valueMapper)`처럼 "원소를 키로, 특정 함수 결과를 값으로" 매핑하는 흔한 패턴은 코틀린 표준 라이브러리의 `associateWith { }` 하나로 대체된다. `java.util.Enumeration`은 `.asSequence()`로 코틀린의 지연 평가 `Sequence`로 변환해서 사용한다.

### 주의사항

- 이건 "패턴 매칭표"에 가깝다 — 모든 `Collectors.xxx`에 1:1 대응 코틀린 함수가 있는 건 아니므로, 다른 Stream 연산을 변환할 때는 그때그때 대응되는 코틀린 컬렉션 함수(`map`, `filter`, `groupBy`, `associateBy` 등)를 찾아 적용해야 한다.

---

## R-012: 롬복 `@Getter`를 코틀린이 인식하지 못하는 문제

- **카테고리**: interop (자바-코틀린 상호운용)
- **도입 강**: [10강](../learning-log/step-10.md) (`PostComment`/`BaseEntity`), [11강](../learning-log/step-11.md)에서 `Member`/`Post`에도 동일 적용
- **적용 조건**: 코틀린 코드(DTO 등)가 Lombok `@Getter`/`@Setter`가 붙은 **자바** 클래스의 필드에 프로퍼티 스타일(`.field`)로 접근할 때

### 문제 상황

`compileKotlin`은 Java 소스를 어노테이션 프로세싱(Lombok 처리) 이전 상태로 직접 분석한다. Lombok이 게터/세터를 생성하는 시점은 `compileJava` 단계인데, Kotlin 컴파일은 그보다 먼저 원본 자바 소스만 보고 끝나기 때문에 Lombok이 만들 예정인 메서드를 "존재하지 않는 메서드"로 인식해 `unresolved reference` 컴파일 에러가 난다.

### 해결 패턴

```diff
 @Entity
-@Getter
 @NoArgsConstructor
 public class SomeEntity {
     private String content;

+    public String getContent() {
+        return content;
+    }
 }
```

`@Getter`/`@Setter` 어노테이션을 제거하고, 코틀린에서 참조가 필요한 필드에 한해 게터(및 필요시 세터)를 **직접 자바 코드로 구현**한다.

### 주의사항

- 클래스의 모든 필드가 아니라, **코틀린 쪽에서 실제로 참조하는 필드**만 우선 게터를 추가하면 된다 (한 번에 전체를 다 손댈 필요 없음 — 이 프로젝트에서도 DTO가 필요로 하는 필드부터 순서대로 처리).
- 최종적으로 해당 클래스 전체를 코틀린으로 변환하면 이 문제 자체가 사라진다 (코틀린 프로퍼티는 게터/세터를 컴파일러가 자동 생성하고, 같은 코틀린 컴파일 유닛 안에서는 순서 문제가 없음). 이 규칙은 "자바 클래스를 아직 코틀린으로 옮기기 전, 과도기"에 필요한 임시 조치다.
- Lombok 자체를 제거하는 것(48강)과는 별개다 — 이 시점에는 아직 Lombok을 쓰되, 코틀린이 참조할 특정 필드에 한해 게터를 수동으로 병행 작성하는 것.

---

## R-013: Java `record` → Kotlin `data class`

- **카테고리**: entity-dto
- **도입 강**: [10강](../learning-log/step-10.md)
- **적용 조건**: 불변 값 객체(주로 DTO)가 자바 `record`로 되어 있을 때

### 변환

```diff
-public record PostCommentDto(
-        int id,
-        String content
-) {
-    public PostCommentDto(PostComment postComment) {
-        this(postComment.getId(), postComment.getContent());
-    }
-}
+data class PostCommentDto(
+    val id: Int,
+    val content: String
+) {
+    constructor(postComment: PostComment) : this(
+        id = postComment.id,
+        content = postComment.content
+    )
+}
```

자바 `record`가 제공하는 것(불변 필드, 자동 `equals`/`hashCode`/`toString`, 컴팩트 생성자)은 코틀린 `data class`가 그대로 대응한다. record의 커스텀 보조 생성자(엔티티를 받아 필드를 채우는 생성자)는 코틀린의 **보조 생성자**(`constructor(...) : this(...)`) 문법으로 옮긴다.

### 주의사항

- record의 컴포넌트 접근자는 `id()`처럼 **괄호 있는 메서드** 형태지만, `data class`의 프로퍼티는 `.id`로 **괄호 없이** 접근한다 — 자바 쪽에서 이 DTO를 호출하는 코드가 있다면 `dto.id()`가 아니라 코틀린이 생성한 JavaBean 스타일 게터 `dto.getId()`를 쓰게 된다(자바에서 호출 시).
- 필드가 전부 불변(`val`)이고 상속을 쓰지 않는 단순 값 객체일 때만 적용한다. 상속이 필요하면 일반 `class`를 유지한다 (13강에서 이 트레이드오프를 다룸).

---

## R-014: 생성자 프로퍼티 어노테이션의 use-site target (`@get:JsonProperty` 등)

- **카테고리**: language-idiom
- **도입 강**: [14강](../learning-log/step-14.md)
- **적용 조건**: 코틀린 주 생성자 프로퍼티에 Jackson(`@JsonProperty` 등) 같이 "어떤 JVM 요소(필드/게터/파라미터)에 붙었는지"가 동작에 영향을 주는 자바 어노테이션을 붙일 때

### 문제 상황

코틀린 생성자 프로퍼티(`val isAdmin: Boolean`) 하나는 컴파일 시 필드 / 생성자 파라미터 / 게터 메서드, 세 가지 JVM 요소로 나뉘어 생성된다. use-site target 없이 어노테이션을 붙이면 기본적으로 생성자 파라미터에 붙는 경우가 많은데, Jackson은 직렬화 시 **게터 메서드**에 붙은 어노테이션만 인식하므로 파라미터에 붙은 어노테이션은 무시되어 조용히 의도와 다르게 동작한다(예: JSON 키가 원하는 이름으로 안 나옴).

### 해결 패턴

```diff
-@JsonProperty("isAdmin")
+@get:JsonProperty("isAdmin")
 val isAdmin: Boolean
```

`@get:`, `@field:`, `@param:`, `@set:`, `@setparam:`, `@delegate:` 등 **use-site target**을 명시해서 어노테이션이 정확히 어디에 붙을지 지정한다. Jackson처럼 게터를 보고 판단하는 라이브러리는 `@get:`을 써야 한다.

### 주의사항

- 8강에서 추가한 `-Xannotation-default-target=param-property` 컴파일러 옵션은 "타겟을 명시 안 했을 때의 기본값"을 바꾸는 것이지, 이 상황처럼 정확히 게터를 겨냥해야 하는 경우를 대신해주지 않는다. Jackson 등 프레임워크에 보이는 어노테이션은 항상 명시적으로 use-site target을 쓰는 게 안전하다.
- 역직렬화(JSON → 객체) 경로에서는 생성자 파라미터 쪽 어노테이션(`@param:`)이 중요할 수 있다 — 방향(직렬화/역직렬화)에 따라 필요한 target이 다를 수 있으므로 실제 사용 방향을 확인할 것.

---

## R-015: `static` 필드/메서드 → `companion object`

- **카테고리**: language-idiom
- **도입 강**: [15강](../learning-log/step-15.md)
- **적용 조건**: 클래스 인스턴스 없이 공유되는 자바 `static` 필드/메서드를 코틀린으로 옮길 때

### 변환

```diff
-public class AppConfig {
-    private static Environment environment;
-
-    public static boolean isDev() {
-        return environment.matchesProfiles("dev");
-    }
-}
+class AppConfig {
+    companion object {
+        private lateinit var environment: Environment
+
+        val isDev: Boolean
+            get() = environment.matchesProfiles("dev")
+    }
+}
```

코틀린엔 `static` 키워드가 없다. 클래스 안에 `companion object { ... }` 블록을 만들면, 그 클래스에 딱 하나 자동으로 딸려오는 싱글턴 객체가 생기고 그 멤버들이 static처럼 동작한다. 값만 계산해서 반환하는 static 메서드는 커스텀 게터가 있는 프로퍼티(`val x: T get() = ...`)로 옮기는 게 관례 — 호출부에서 괄호 없이 `AppConfig.isDev`처럼 접근.

### 주의사항

- 생성자 파라미터명과 companion 멤버명이 같으면 파라미터가 이름을 가려서, companion 멤버에 대입할 때 `Companion.멤버명 = ...`처럼 명시적으로 써야 한다.
- **자바 코드에서 이 companion object 멤버를 호출하려면** `AppConfig.Companion.isDev()`처럼 어색하게 접근해야 한다. `@JvmStatic`으로 이 문제를 해결한다 ([16강](../learning-log/step-16.md) 참고).

---

## R-016: Nullable + `!!` 단언 → `lateinit`

- **카테고리**: language-idiom
- **도입 강**: [15강](../learning-log/step-15.md)
- **적용 조건**: "생성 시점엔 값이 없지만, 실제 사용 시점엔 항상 채워져 있다고 보장할 수 있는" non-null 타입 프로퍼티 (주로 DI로 나중에 채워지는 필드)

### 변환

```diff
-private var environment: Environment? = null
-// 사용할 때마다:
-environment!!.matchesProfiles("dev")
+private lateinit var environment: Environment
+// 사용할 때:
+environment.matchesProfiles("dev")
```

`lateinit`은 "지금 초기화하지 않아도 되지만, 실제로 쓰기 전엔 반드시 값을 채워 넣겠다"는 컴파일러와의 약속이다. 타입은 nullable(`?`)이 아닌 확정 non-null로 선언하므로, 사용하는 곳마다 `!!`(non-null 단언, 틀리면 크래시)를 안 붙여도 된다.

### 주의사항

- `lateinit`은 `var`에만 쓸 수 있고(재할당 가능해야 함), 원시 타입(`Int`, `Boolean` 등)에는 쓸 수 없다(참조 타입만 가능).
- 값이 채워지기 전에 접근하면 `NullPointerException`이 아니라 `UninitializedPropertyAccessException`이 발생 — 원인 파악이 더 쉬운 에러 메시지를 준다.
- "정말로 나중에 채워지는 게 보장되는가"를 확신할 수 없다면 억지로 `lateinit`을 쓰지 말고 nullable(`?`)을 유지하는 게 안전하다.

---

## R-017: 세터 주입 + `@PostConstruct` → 생성자 주입 + `init` 블록

- **카테고리**: spring-di
- **도입 강**: [15강](../learning-log/step-15.md)
- **적용 조건**: `@Autowired`가 붙은 세터 메서드 + `@PostConstruct` 초기화 메서드 조합으로 되어있는 Spring 빈

### 변환

```diff
-class AppConfig {
-    @Autowired
-    fun setEnvironment(environment: Environment) { ... }
-
-    @Autowired
-    fun setObjectMapper(objectMapper: ObjectMapper?) { ... }
-
-    @PostConstruct
-    fun postConstruct() { ... }
-}
+class AppConfig(
+    environment: Environment,
+    objectMapper: ObjectMapper
+) {
+    init {
+        // 세터들 + postConstruct에서 하던 일을 여기 한 곳에
+    }
+}
```

생성자 파라미터로 의존성을 즉시 받으면, 객체가 존재하는 순간부터 이미 모든 의존성이 채워진 상태로 시작한다("불완전한 중간 상태"가 없음). `init { }` 블록은 객체 생성 시점에 한 번 실행되는 초기화 코드로, 별도의 생명주기 어노테이션(`@PostConstruct`) 없이 그 역할을 대신한다.

### 주의사항

- 이 패턴은 R-016(`lateinit`)과 함께 쓰일 때 특히 유용하다 — 생성자 주입이 보장되므로 `lateinit` 필드가 "사용 전에 항상 채워져 있다"는 약속을 지키기 쉬워진다.
- Spring은 생성자가 하나뿐인 클래스에는 `@Autowired` 없이도 자동으로 생성자 주입을 해준다.

---

## 확장 메모

<!-- 강이 진행되며 새로 검증된 규칙을 이 아래(또는 새 섹션)에 계속 추가할 것 -->
