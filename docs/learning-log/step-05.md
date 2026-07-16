# step-05: 스프링 이니셜라이저로 자프링 vs 코프링 build.gradle.kts 비교

- 강의 링크: https://www.slog.gg/p/14128#5강
- 상태: 완료

## 요구사항 요약

[start.spring.io](https://start.spring.io)에서 Language를 Java ↔ Kotlin으로 바꿀 때 생성되는 `build.gradle.kts` 차이를 비교하는 강의 (코드 타이핑 없음, 개념 미리보기).

| 항목 | Java | Kotlin |
|---|---|---|
| `plugins {}` | `java` | `kotlin("jvm")`, `kotlin("plugin.spring")` 추가 |
| JPA 사용 시 | 없음 | `kotlin("plugin.jpa")` 추가 |
| `dependencies` | 없음 | `jackson-module-kotlin`, `kotlin-reflect` 추가 |
| 컴파일 옵션 | 없음 | `tasks.withType<KotlinCompile>` 블록 추가 |

현재 `back/build.gradle.kts`는 아직 `java` 플러그인 + Lombok만 사용 중. 이 표의 항목들을 6~8강에서 실제로 추가하며 각각의 필요성을 체감할 예정.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
