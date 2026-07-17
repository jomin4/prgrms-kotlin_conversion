# 자바 → 코틀린 전환 규칙 (팀 프로젝트 적용용)

이 문서는 `kotlin_conversion` 학습 프로젝트에서 **직접 확인하고 이해한 변환 패턴만** 규칙으로 뽑아 기록합니다.
각 규칙은 팀원의 자바 기반 프로젝트에 에이전트가 적용할 때 사용할 수 있도록, 다른 프로젝트에도 일반화 가능한 형태로 씁니다.

**적용 스코프 주의**: 여기 실린 규칙은 **아래 명시된 강(N강)까지 학습 완료된 것만** 포함합니다.
에이전트에게 이 문서를 넘길 때는 반드시 "이 문서에 있는 규칙만 적용하고, 문서에 없는 패턴은 임의로 적용하지 말 것"을 지시하세요.

- 현재 커버리지: **7강까지**
- 상세 학습 과정/트러블슈팅은 `docs/learning-log/step-NN.md` 참고 (규칙 하나당 어느 강에서 나왔는지 링크됨)

---

## 규칙 목록

| ID | 제목 | 카테고리 | 도입 강 |
|---|---|---|---|
| [R-001](#r-001-gradle-빌드에-kotlin-jvm-플러그인-추가) | Gradle 빌드에 Kotlin JVM 플러그인 추가 | build-config | 6강 |
| [R-002](#r-002-javakotlin-컴파일-jvm-타겟-정합성-맞추기) | Java/Kotlin 컴파일 JVM 타겟 정합성 맞추기 | build-config | 6강 |
| [R-003](#r-003-애플리케이션-진입점-main-변환) | 애플리케이션 진입점(`main`) 변환 | entrypoint | 6강 |
| [R-004](#r-004-spring-빈-클래스는-open이어야-함--kotlinplugin.spring) | Spring 빈 클래스는 `open`이어야 함 → `kotlin("plugin.spring")` | build-config | 7강 |

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

## 확장 메모

<!-- 강이 진행되며 새로 검증된 규칙을 이 아래(또는 새 섹션)에 계속 추가할 것 -->
