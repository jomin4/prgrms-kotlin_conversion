# step-19: RsData 변환

- 강의 링크: https://www.slog.gg/p/14128#19강
- 상태: 완료

## 요구사항 요약

`RsData.java`(record) 삭제 → `back/src/main/kotlin/com/back/global/rsData/RsData.kt`로 `@JvmRecord data class` 변환.

### 새로운 개념

- **`@JvmRecord`**: `data class`를 진짜 `java.lang.Record`로 컴파일되게 하는 옵트인 어노테이션. 없으면 평범한 클래스(JavaBean 게터, `getStatusCode()`)가 되고, 있으면 진짜 record(접근자 `statusCode()`, `get` 없음)가 됨. Kotlin 1.5+/JVM 16+ 필요(우리 프로젝트는 충족).
- **왜 필요했나**: grep으로 확인한 결과 `ResponseAspect.java`, `CustomAuthenticationFilter.java`, `GlobalExceptionHandler.java`, `CustomOAuth2UserService.java`가 전부 `rsData.statusCode()`, `.data()` 같은 **record 스타일 호출**(`get` 없음)을 이미 쓰고 있어서, `@JvmRecord` 없이는 전부 컴파일 에러가 났을 것.
- **`@field:JsonIgnore` (14강의 `@get:`과 다름)**: Jackson은 진짜 자바 record를 다룰 때 게터가 아니라 **필드(레코드 컴포넌트)** 기준으로 어노테이션을 인식하므로, `@JvmRecord`가 붙은 클래스에서는 `@get:` 대신 `@field:`을 써야 함. use-site target 선택은 "평범한 data class냐 record냐"에 따라 달라짐.
- **`resultCode.split("-", limit = 2)[0].toInt()`**: `String.split(regex, limit)`의 위치 인자 → 코틀린 named argument(`limit =`)로 명시. `Integer.parseInt(x)` → `x.toInt()` 확장 함수.
- **`@JvmOverloads`(생성자에 적용)**: 17강(R-020)과 같은 원리를 **보조 생성자**에 적용. `GlobalExceptionHandler.java` 등에서 실제로 2개 인자(`resultCode`, `msg`)만 넘기는 자바 호출이 다수 있어 확인 후 적용.

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인 — record 스타일 호출부 전부 무수정으로 정상 컴파일.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

### 질문1
- **Q.** 자바 record와 코틀린 data class가 같은 역할인데, `kotlin("jvm")` 설정했으니 `@JvmRecord` 없이도 자동으로 되는 거 아닌가?
- **A.** `kotlin("jvm")` 플러그인은 `.kt`를 JVM 바이트코드로 컴파일하는 빌드 설정일 뿐, 클래스의 부모/접근자 모양과는 무관. `data class`는 자바 record보다 먼저 나온 기능이라 기본적으로 `java.lang.Object` 상속 + JavaBean 게터로 컴파일됨. `@JvmRecord`는 "진짜 `java.lang.Record`로 만들어라"는 별도 옵트인 어노테이션. grep으로 확인한 결과 우리 코드베이스에 record 스타일 호출(`rsData.statusCode()`)이 실제로 있어서 이번엔 꼭 필요했음.

### 질문2
- **Q.** 현재 data class와 record 차이점을 표로 정리
- **A.** 상속 관계(Object vs Record), 접근자 이름(getXxx vs xxx), Jackson use-site target(@get: vs @field:), 요구 버전(Kotlin 1.5+/JVM 16+) 등을 표로 정리해서 답변. (본문 채팅 참고)
