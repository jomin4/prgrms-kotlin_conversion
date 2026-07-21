# 자바 → 코틀린 전환 규칙 (팀 프로젝트 적용용)

이 문서는 `kotlin_conversion` 학습 프로젝트에서 **직접 확인하고 이해한 변환 패턴만** 규칙으로 뽑아 기록합니다.
각 규칙은 팀원의 자바 기반 프로젝트에 에이전트가 적용할 때 사용할 수 있도록, 다른 프로젝트에도 일반화 가능한 형태로 씁니다.

**적용 스코프 주의**: 여기 실린 규칙은 **아래 명시된 강(N강)까지 학습 완료된 것만** 포함합니다.
에이전트에게 이 문서를 넘길 때는 반드시 "이 문서에 있는 규칙만 적용하고, 문서에 없는 패턴은 임의로 적용하지 말 것"을 지시하세요.

- 현재 커버리지: **21강까지** (13강은 12강에서 선반영되어 규칙 추가 없음)
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
| [R-018](#r-018-jvmstatic으로-자바-호출-호환성-유지) | `@JvmStatic`으로 자바 호출 호환성 유지 | interop | 16강 |
| [R-019](#r-019-static-유틸-클래스--object-선언) | static 유틸 클래스 → `object` 선언 | language-idiom | 17강 |
| [R-020](#r-020-jvmoverloads로-디폴트-파라미터를-자바-오버로드로-노출) | `@JvmOverloads`로 디폴트 파라미터를 자바 오버로드로 노출 | interop | 17강 |
| [R-021](#r-021-varargs--vararg) | 가변인자(`varargs`) → `vararg` + 스프레드 연산자 | language-idiom | 17강 |
| [R-022](#r-022-로케일-안전한-문자열-대소문자-변환) | 로케일 안전한 문자열 대소문자 변환 | language-idiom | 17강 |
| [R-023](#r-023-자바-io-보일러플레이트--코틀린-확장-함수) | 자바 I/O 보일러플레이트 → 코틀린 확장 함수 | language-idiom | 17강 |
| [R-024](#r-024-optional-체인--안전-호출--엘비스-연산자) | `Optional` 체인 → 안전 호출(`?.`) + 엘비스 연산자(`?:`) | language-idiom | 18강 |
| [R-025](#r-025-instanceof--캐스팅--is-스마트-캐스트) | `instanceof` + 캐스팅 → `is` 스마트 캐스트 | language-idiom | 18강 |
| [R-026](#r-026-스코프-함수-letapply로-보일러플레이트-압축) | 스코프 함수 `let`/`apply`로 보일러플레이트 압축 | language-idiom | 18강 |
| [R-027](#r-027-firstornull-takeif-컬렉션조건-관용구) | `firstOrNull`/`takeIf` 컬렉션·조건 관용구 | language-idiom | 18강 |
| [R-028](#r-028-불변조건-위반-시-null-대신-예외) | 불변조건 위반 시 null 대신 예외 | design | 18강 |
| [R-029](#r-029-jvmrecord로-진짜-자바-record-유지) | `@JvmRecord`로 진짜 자바 record 유지 | interop | 19강 |
| [R-030](#r-030-jackson-use-site-target-선택-get-vs-field) | Jackson use-site target 선택: `@get:` vs `@field:` | interop | 19강 |
| [R-031](#r-031-spring-security-코틀린-dsl-http--) | Spring Security 코틀린 DSL (`http { }`) | spring-di | 20강 |
| [R-032](#r-032-sam-인터페이스를-람다로-직접-생성) | SAM 인터페이스를 람다로 직접 생성 | language-idiom | 20강 |
| [R-033](#r-033-상속--인터페이스-구현--부모-생성자-호출을-한-줄로) | 상속 + 인터페이스 구현 + 부모 생성자 호출을 한 줄로 | language-idiom | 21강 |
| [R-034](#r-034-collection-extends-t--collectiont-선언-지점-가변성) | `Collection<? extends T>` → `Collection<T>` (선언 지점 가변성) | language-idiom | 21강 |

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

## R-018: `@JvmStatic`으로 자바 호출 호환성 유지

- **카테고리**: interop (자바-코틀린 상호운용)
- **도입 강**: [16강](../learning-log/step-16.md)
- **적용 조건**: `companion object` 멤버를, **아직 자바로 남아있는 다른 파일들이** `클래스명.메서드명()` 형태(예전 `static` 호출 방식)로 호출해야 할 때 — 마이그레이션 도중이 아니라면 필수는 아님

### 문제 상황

`companion object { val isDev ... }`는 바이트코드 상 진짜 `static`이 아니라, `Companion`이라는 별도 싱글턴 객체의 **인스턴스 멤버**로 컴파일된다. 그래서 자바 코드에서는 `AppConfig.Companion.isDev()`처럼 `Companion`을 거쳐야만 호출할 수 있다 — 클래스를 코틀린으로 바꿨다는 이유만으로 이걸 참조하는 모든 자바 호출부를 찾아 고쳐야 하는 상황이 생긴다.

### 해결 패턴

```diff
 companion object {
+    @JvmStatic
     val isDev: Boolean
         get() = environment.matchesProfiles("dev")
 }
```

`@JvmStatic`을 붙이면 컴파일러가 `Companion` 안의 멤버 외에 **바깥 클래스에 진짜 static 메서드를 하나 더 생성**한다. 그래서 자바 코드는 원래(순수 자바 시절)와 동일하게 `AppConfig.isDev()`로 호출할 수 있게 되고, 호출부를 전혀 안 고쳐도 된다. 코틀린 쪽 호출(`AppConfig.isDev`)은 애초에 문제없이 동작하므로 영향받지 않는다.

### 주의사항

- 자바/코틀린이 혼재된 점진적 마이그레이션 중에는 companion object를 만들 때마다 "이걸 아직 자바인 다른 파일이 참조할 가능성이 있는가"를 먼저 확인하고, 그렇다면 `@JvmStatic`을 기본으로 붙이는 게 안전하다.
- 프로젝트 전체가 코틀린으로 다 옮겨진 뒤(더 이상 자바에서 호출할 일이 없는 시점)라면 `@JvmStatic`은 불필요한 보일러플레이트가 될 수 있다.

---

## R-019: static 유틸 클래스 → `object` 선언

- **카테고리**: language-idiom
- **도입 강**: [17강](../learning-log/step-17.md)
- **적용 조건**: 인스턴스를 만들 필요 없이 모든 멤버가 `static`인 자바 유틸 클래스

### 변환

```diff
-public class json {
-    public static ObjectMapper objectMapper;
-    public static String toString(Object object) { ... }
-}
+object json {
+    lateinit var objectMapper: ObjectMapper
+
+    @JvmStatic
+    fun toString(obj: Any): String? { ... }
+}
```

`class` 대신 `object`로 선언하면 코틀린이 자동으로 유일한 인스턴스(싱글턴)를 만들어준다. 자바로 컴파일되면 `public static final X INSTANCE = new X()` + `private X() {}` 패턴과 동일.

### 주의사항

- `object`의 멤버도 바이트코드상 `INSTANCE`의 **인스턴스 메서드**다. R-018과 동일하게, 자바에서 이 멤버를 호출해야 한다면 `@JvmStatic`을 붙여야 `Ut.json.toString(x)`처럼 자연스럽게 호출 가능하다(없으면 `Ut.json.INSTANCE.toString(x)`).
- 어디에 `@JvmStatic`이 필요한지는 감으로 짐작하지 말고, **grep으로 실제 자바 호출부를 확인**한 뒤 결정한다(R-018 참고) — 필요 없는 곳까지 붙일 필요는 없다.

---

## R-020: `@JvmOverloads`로 디폴트 파라미터를 자바 오버로드로 노출

- **카테고리**: interop (자바-코틀린 상호운용)
- **도입 강**: [17강](../learning-log/step-17.md)
- **적용 조건**: 코틀린 디폴트 파라미터(`param: T = default`)가 있는 함수를, 인자를 생략한 형태로 자바에서도 호출해야 할 때

### 문제 상황

자바에는 "파라미터 기본값" 문법이 없다. `@JvmOverloads` 없이 컴파일하면 자바 바이트코드엔 풀파라미터 메서드 하나만 생기고, 인자를 생략한 자바 호출은 컴파일 에러가 난다.

### 해결 패턴

```diff
+@JvmOverloads
 fun toString(obj: Any, defaultValue: String? = null): String? { ... }
```

`@JvmOverloads`를 붙이면 파라미터 개수별로 오버로드 메서드가 자동 생성되어, 자바에서 `toString(obj)`와 `toString(obj, "값")` 둘 다 호출 가능해진다.

### 주의사항

- 실제로 인자를 생략한 자바 호출부가 있는지 grep으로 확인하고 필요한 곳에만 붙인다(R-018/R-019와 같은 원칙).

---

## R-021: 가변인자(`varargs`) → `vararg` + 스프레드 연산자

- **카테고리**: language-idiom
- **도입 강**: [17강](../learning-log/step-17.md)
- **적용 조건**: 자바의 `T... args` 가변인자 파라미터를 코틀린으로 옮기거나, 배열을 가변인자 자리에 전달할 때

### 변환

```diff
-public static void run(String... args) { ... }
-public static void runAsync(String... args) {
-    new Thread(() -> run(args)).start();
-}
+fun run(vararg args: String) { ... }
+fun runAsync(vararg args: String) {
+    Thread(Runnable { run(*args) }).start()
+}
```

함수 **선언**은 자바 가변인자와 1:1 대응(`vararg`). 하지만 이미 배열로 갖고 있는 값을 다른 vararg 파라미터 자리에 **전달**할 때는 스프레드 연산자(`*`)로 풀어서 넘겨야 한다 — 자바는 배열을 그대로 넘기면 되지만 코틀린은 `*` 없이 배열을 vararg 자리에 넣으면 타입 불일치로 컴파일 에러가 난다.

### 주의사항

- `*`는 "이 배열의 원소들을 낱개 인자로 풀어서 전달하라"는 뜻이며, 다른 언어의 spread와 유사하다 (6강 `runApplication<T>(*args)`와 동일한 문법).

---

## R-022: 로케일 안전한 문자열 대소문자 변환

- **카테고리**: language-idiom
- **도입 강**: [17강](../learning-log/step-17.md)
- **적용 조건**: 자바의 인자 없는 `String.toLowerCase()`/`toUpperCase()`를 코틀린으로 옮길 때

### 변환

```diff
-someString.toLowerCase()
+someString.lowercase(Locale.getDefault())
```

문자 대소문자 변환 규칙은 언어(로케일)에 따라 달라질 수 있다(예: 터키어 로케일에서 `"I".lowercase()`는 `"i"`가 아니라 `"ı"`). 자바의 인자 없는 `toLowerCase()`는 시스템 기본 로케일을 암묵적으로 사용해 환경별로 다르게 동작할 위험이 있다. 코틀린은 인자 없는 버전을 지원하지 않고 로케일 명시를 강제한다.

### 주의사항

- 원본 자바 코드와 동일한 동작을 유지하려면 `Locale.getDefault()`를 쓴다.
- 로케일에 상관없이 항상 일관되게 동작해야 하는 경우(예: 프로토콜 문자열, 파일 확장자 비교 등 사람 언어와 무관한 값)라면 `Locale.ROOT`를 쓰는 게 더 안전하다.

---

## R-023: 자바 I/O 보일러플레이트 → 코틀린 확장 함수

- **카테고리**: language-idiom
- **도입 강**: [17강](../learning-log/step-17.md)
- **적용 조건**: `InputStream`/`Reader`를 `BufferedReader`로 감싸서 한 줄씩 읽는 자바 코드를 코틀린으로 옮길 때

### 변환

```diff
-try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
-    String line;
-    while ((line = reader.readLine()) != null) {
-        System.out.println(line);
-    }
-}
+stream.bufferedReader().useLines { lines ->
+    lines.forEach { println(it) }
+}
```

코틀린 표준 라이브러리가 `InputStream`/`Reader`에 추가해주는 확장 함수(`bufferedReader()`, `useLines { }`)로 래핑+반복+자원 해제를 한 번에 표현한다. `useLines`는 `Closeable.use`와 같은 원리로 블록이 끝나면 자동으로 리더를 닫아준다(try-with-resources와 동일 효과). `lines`는 지연 평가되는 `Sequence<String>`이라 대용량 출력에도 메모리 효율적이다.

### 주의사항

- 한 번 소비하면 다시 순회할 수 없는 시퀀스이므로(스트림 기반), 같은 라인 목록을 여러 번 순회해야 한다면 먼저 `toList()` 등으로 리스트화해야 한다.

---

## R-024: `Optional` 체인 → 안전 호출(`?.`) + 엘비스 연산자(`?:`)

- **카테고리**: language-idiom
- **도입 강**: [18강](../learning-log/step-18.md)
- **적용 조건**: `Optional.ofNullable(x).map(...).filter(...).orElse(default)` 형태의 null 처리 체인

### 변환

```diff
-return Optional.ofNullable(req.getHeader(name))
-        .filter(headerValue -> !headerValue.isBlank())
-        .orElse(defaultValue);
+return req.getHeader(name) ?: defaultValue
```

`?.`(안전 호출)은 "왼쪽이 null이면 이후 체인을 건너뛰고 null이 된다"는 뜻, `?:`(엘비스 연산자)는 "왼쪽이 null이면 오른쪽 값을 대신 쓴다"는 뜻이다. 코틀린은 null 안정성이 언어 문법에 내장돼 있어서, 자바의 `Optional` 래핑 없이도 같은 안전성을 훨씬 짧게 표현한다.

### 주의사항

- `Optional`은 자바 라이브러리(JPA `findById` 등)가 반환하는 경우 여전히 그대로 받아 처리해야 한다 — 이 규칙은 "코틀린 코드 내부에서 새로 null 체크 체인을 짤 때"에 해당한다.

---

## R-025: `instanceof` + 캐스팅 → `is` 스마트 캐스트

- **카테고리**: language-idiom
- **도입 강**: [18강](../learning-log/step-18.md)
- **적용 조건**: 타입을 확인한 뒤 그 타입으로 캐스팅해서 멤버에 접근하는 자바 코드

### 변환

```diff
-if (principal instanceof SecurityUser) {
-    SecurityUser securityUser = (SecurityUser) principal;
-    return securityUser.getId();
-}
+if (principal is SecurityUser) {
+    return principal.id   // 캐스팅 없이 바로 접근
+}
```

`is`로 타입을 확인한 블록 안에서는 코틀린이 자동으로 그 타입인 것처럼 취급해준다(스마트 캐스트). 별도의 캐스팅 구문이 필요 없다.

### 주의사항

- 스마트 캐스트는 해당 변수가 그 블록 안에서 변경되지 않는다는 걸 컴파일러가 보장할 수 있을 때만 동작한다(`val`이거나, `var`라도 재할당되지 않는 지역 변수 등). `var` 프로퍼티가 중간에 바뀔 수 있는 경우엔 스마트 캐스트가 안 먹혀서 명시적 캐스팅(`as`)이 필요할 수 있다.

---

## R-026: 스코프 함수 `let`/`apply`로 보일러플레이트 압축

- **카테고리**: language-idiom
- **도입 강**: [18강](../learning-log/step-18.md)
- **적용 조건**: "null이 아닐 때만 처리"(`let`) 또는 "객체를 만들면서 여러 속성을 설정"(`apply`)하는 자바 코드

### 변환

```diff
-Cookie cookie = new Cookie(name, value);
-cookie.setPath("/");
-cookie.setHttpOnly(true);
-cookie.setSecure(true);
+val cookie = Cookie(name, value).apply {
+    path = "/"
+    isHttpOnly = true
+    secure = true
+}
```

```diff
-if (principal != null) {
-    return doSomething(principal);
-}
-return null;
+return principal?.let { doSomething(it) }
```

`apply { }`는 객체를 만든 직후 그 객체(`this`)를 대상으로 여러 설정을 이어붙이고 객체 자신을 반환한다. `let { }`은 값이 null이 아닐 때만 블록을 실행해 그 결과를 반환한다(`?.let { }` 조합으로 자주 쓰임).

### 주의사항

- 코틀린 스코프 함수는 `let`/`apply`/`run`/`with`/`also` 다섯 가지가 있고 미묘하게 다르다 — `apply`/`also`는 수신 객체 자신을 반환(설정 후 그 객체를 계속 쓸 때), `let`/`run`은 블록의 결과를 반환(값을 변환할 때)한다는 정도만 우선 기억해두면 충분하다.

---

## R-027: `firstOrNull`/`takeIf` 컬렉션·조건 관용구

- **카테고리**: language-idiom
- **도입 강**: [18강](../learning-log/step-18.md)
- **적용 조건**: `stream().filter(...).findFirst().orElse(null)` 또는 "조건에 맞으면 값 유지, 아니면 null" 패턴

### 변환

```diff
-return Arrays.stream(cookies)
-        .filter(cookie -> name.equals(cookie.getName()))
-        .map(Cookie::getValue)
-        .filter(value -> value != null && !value.isBlank())
-        .findFirst()
-        .orElse(defaultValue);
+return cookies
+    ?.firstOrNull { it.name == name }
+    ?.value
+    ?.takeIf { it.isNotBlank() }
+    ?: defaultValue
```

`firstOrNull { 조건 }`은 조건에 맞는 첫 원소(없으면 null), `takeIf { 조건 }`은 조건이 참이면 그 값 그대로 유지하고 거짓이면 null로 바꾼다. `?:`(R-024)와 조합해 "조건 안 맞으면 기본값" 패턴을 짧게 표현한다.

---

## R-028: 불변조건 위반 시 null 대신 예외

- **카테고리**: design (동작이 바뀌는 결정 — 기계적 변환이 아니므로 적용 전 실제 호출부 확인 필수)
- **도입 강**: [18강](../learning-log/step-18.md)
- **적용 조건**: "이 값이 없으면 안 되는 게 당연한" 상황(예: 인증이 보장된 경로에서 로그인 사용자 조회)에서 자바 코드가 관행적으로 `null`을 반환하던 경우

### 변환

```diff
-public Member getActor() {
-    // ...
-    return null;   // 없으면 조용히 null
-}
+val actor: Member
+    get() = // ...
+        ?: throw IllegalStateException("인증된 사용자가 없습니다.")   // 없으면 즉시 예외
```

"null이 나올 수 있는 타입"으로 두고 호출부마다 null 체크를 강제하는 대신, "이 상황에서 null은 프로그램의 버그"라고 판단되면 non-null 타입으로 선언하고 실패 시 명확한 예외를 던진다. 조용한 NPE보다 원인이 드러나는 예외가 디버깅에 유리하다.

### 주의사항

- **기계적 변환이 아니라 설계 판단**이다 — 적용 전 실제 호출부를 grep으로 확인해서, null을 실제로 의미 있게 처리하는 곳이 있는지 반드시 확인한 뒤 적용한다. 하나라도 null을 기대하는 호출부가 있다면 이 규칙을 적용하면 안 된다.

---

## R-029: `@JvmRecord`로 진짜 자바 record 유지

- **카테고리**: interop (자바-코틀린 상호운용)
- **도입 강**: [19강](../learning-log/step-19.md)
- **적용 조건**: 변환 대상이 원래 자바 `record`였고, **자바 코드에서 이미 record 스타일 접근자(`.xxx()`, `get` 없음)를 호출하는 곳이 있을 때**

### 배경

`data class`는 자바 record보다 먼저 나온 기능이라, 기본적으로 `java.lang.Object`를 상속하는 평범한 클래스(JavaBean 게터 `getXxx()`)로 컴파일된다. `@JvmRecord`를 붙여야 진짜 `java.lang.Record`를 상속하고, 접근자도 record 관례(`xxx()`, `get` 없음)로 생성된다.

### 변환

```diff
-public record RsData<T>(String resultCode, int statusCode, String msg, T data) { ... }
+@JvmRecord
+data class RsData<T>(val resultCode: String, val statusCode: Int, val msg: String, val data: T?) { ... }
```

### 적용 여부 판단 방법

grep으로 자바 호출부를 확인한다:
```
grep -rn "\.statusCode()\|\.resultCode()\|\.data()" *.java
```
- record 스타일 호출(`get` 없음)이 있으면 → `@JvmRecord` 필요
- 생성자 호출뿐이거나 없으면 → 굳이 안 붙여도 됨(일반 `data class`로 충분, R-013)

### 주의사항

- Kotlin 1.5+, JVM 타겟 16+ 필요.
- 모든 컴포넌트가 `val`이어야 하고, 다른 클래스를 상속할 수 없다(record 자체의 제약).
- 10~14강에서 변환한 다른 record 기반 DTO들(`PostCommentDto` 등)은 record 스타일 호출이 없어서 `@JvmRecord`를 안 붙였다 — "원래 record였다"는 사실만으로 자동 적용하는 규칙이 아니라, **실제 호출부 확인이 먼저**다.

---

## R-030: Jackson use-site target 선택: `@get:` vs `@field:`

- **카테고리**: interop (자바-코틀린 상호운용)
- **도입 강**: [19강](../learning-log/step-19.md)
- **적용 조건**: Jackson 어노테이션(`@JsonProperty`, `@JsonIgnore` 등)을 코틀린 생성자 프로퍼티에 붙일 때 — R-014의 확장/구체화

### 규칙

| 클래스 종류 | 올바른 use-site target |
|---|---|
| 평범한 `data class` (`@JvmRecord` 없음) | `@get:` (Jackson이 JavaBean 게터를 보고 판단) |
| `@JvmRecord data class` (진짜 자바 record) | `@field:` (Jackson이 record는 필드/컴포넌트 기준으로 판단) |

```diff
-@JvmRecord
 data class MemberDto(
-    @field:JsonProperty("isAdmin") val isAdmin: Boolean
+    @get:JsonProperty("isAdmin") val isAdmin: Boolean
 )

+@JvmRecord
 data class RsData<T>(
-    @get:JsonIgnore val statusCode: Int
+    @field:JsonIgnore val statusCode: Int
 )
```

### 주의사항

- 잘못된 target을 쓰면 컴파일은 되지만 **Jackson이 어노테이션을 조용히 무시**하는 형태로 실패하므로(런타임에만 드러남), 클래스가 `@JvmRecord`인지 아닌지부터 먼저 확인하고 target을 고를 것.

---

## R-031: Spring Security 코틀린 DSL (`http { }`)

- **카테고리**: spring-di
- **도입 강**: [20강](../learning-log/step-20.md)
- **적용 조건**: `SecurityFilterChain` 빈을 자바의 `HttpSecurity` 메서드 체이닝(`.csrf(...).oauth2Login(...)`) 스타일로 구성한 코드를 코틀린으로 옮길 때

### 변환

```diff
-http
-        .csrf(AbstractHttpConfigurer::disable)
-        .oauth2Login(oauth2Login -> oauth2Login.successHandler(handler));
-return http.build();
+import org.springframework.security.config.annotation.web.invoke
+
+http {
+    csrf { disable() }
+    oauth2Login {
+        authenticationSuccessHandler = handler
+    }
+}
+return http.build()
```

`org.springframework.security.config.annotation.web.invoke`를 import하면 `HttpSecurity`에 `operator fun invoke(configure: HttpSecurity.() -> Unit)` 확장이 활성화되어, `http(...)` 대신 `http { ... }`(중괄호 호출) 문법을 쓸 수 있다. 블록 내부는 수신 객체 지정 람다(`HttpSecurity.() -> Unit`)라서 `authorizeHttpRequests { }`, `csrf { }` 등을 메서드 체이닝 없이 나란히 나열할 수 있다.

### 주의사항

- 자바에서 여러 경로를 한 번에 받던 `requestMatchers(method, "p1", "p2", "p3")` 같은 vararg 오버로드는, DSL의 `authorize(pattern, access)`가 패턴을 하나만 받으므로 경로 개수만큼 줄을 나눠 반복해야 한다.
- 프로젝트 고유의 설정(CORS 연결 여부, 허용 메서드 목록 등)은 강의 원본과 다를 수 있으므로, 변환 전 실제 자바 파일을 반드시 대조해서 빠뜨리지 않아야 한다.

---

## R-032: SAM 인터페이스를 람다로 직접 생성

- **카테고리**: language-idiom
- **도입 강**: [20강](../learning-log/step-20.md)
- **적용 조건**: 메서드가 하나뿐인 함수형 인터페이스(`AuthenticationEntryPoint`, `AccessDeniedHandler` 등)를 자바 익명 클래스/람다로 구현한 코드

### 변환

```diff
-(request, response, authException) -> {
-    response.setStatus(401);
-}
+AuthenticationEntryPoint { _, response, _ ->
+    response.status = 401
+}
```

코틀린에서 자바 함수형 인터페이스는 `인터페이스이름 { 람다본문 }` 형태로 바로 인스턴스를 만들 수 있다(SAM 변환). 안 쓰는 파라미터는 `_`로 표시해 "이건 의도적으로 안 쓴다"는 걸 드러낸다.

---

## R-033: 상속 + 인터페이스 구현 + 부모 생성자 호출을 한 줄로

- **카테고리**: language-idiom
- **도입 강**: [21강](../learning-log/step-21.md)
- **적용 조건**: 자바에서 클래스를 상속하고 생성자 안에서 `super(...)`를 호출하며, 동시에 인터페이스도 구현하는 경우

### 변환

```diff
-public class SecurityUser extends User implements OAuth2User {
-    public SecurityUser(int id, String username, String nickname, Collection<? extends GrantedAuthority> authorities) {
-        super(username, "", authorities);
-        this.id = id;
-        this.nickname = nickname;
-    }
-}
+class SecurityUser(
+    val id: Int,
+    username: String,
+    val nickname: String,
+    authorities: Collection<GrantedAuthority>
+) : User(username, "", authorities), OAuth2User {
+}
```

코틀린은 `class X(생성자 파라미터...) : 부모클래스(부모생성자인자...), 인터페이스1, 인터페이스2` 형태로, 상속·부모 생성자 호출·인터페이스 구현을 클래스 선언 한 줄에 표현한다. 생성자 파라미터에 `val`/`var`를 붙이면 그 클래스의 프로퍼티가 되고, 안 붙이면 부모 생성자 등에 전달만 하고 버려진다 — "이 값을 나중에도 계속 참조해야 하는가"로 판단한다.

---

## R-034: `Collection<? extends T>` → `Collection<T>` (선언 지점 가변성)

- **카테고리**: language-idiom
- **도입 강**: [21강](../learning-log/step-21.md)
- **적용 조건**: 자바의 `Collection<? extends T>`(읽기 전용 목적의 상한 와일드카드)를 코틀린으로 옮길 때

### 변환

```diff
-Collection<? extends GrantedAuthority> authorities
+Collection<GrantedAuthority> authorities
```

자바 제네릭은 기본적으로 불변(invariant)이라 `List<Dog>`가 `List<Animal>`로 취급되지 않는다(추가 메서드가 있어 안전하지 않으므로). 그래서 "읽기만 할 것"임을 사용하는 자리마다 `? extends`로 표시해야 한다(사용 지점 가변성). 코틀린의 `Collection<out E>`는 애초에 `add(E)` 같은 메서드가 없는 읽기 전용 인터페이스로 **선언 시점에** 이미 공변으로 정의돼 있어서, 어디서 쓰든 와일드카드 없이 자동으로 하위 타입 컬렉션을 받을 수 있다(선언 지점 가변성).

### 주의사항

- `MutableCollection<T>`처럼 `add(T)`가 있는 타입은 이 트릭이 안 통한다 — 그런 경우엔 코틀린에서도 사용 지점에 `out T`/`in T`를 직접 명시해야 한다.

---

## 확장 메모

<!-- 강이 진행되며 새로 검증된 규칙을 이 아래(또는 새 섹션)에 계속 추가할 것 -->
