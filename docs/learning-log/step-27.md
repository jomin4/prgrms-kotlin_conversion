# step-27: SpringDocConfig 변환

- 강의 링크: https://www.slog.gg/p/14128#27강
- 상태: 완료

## 요구사항 요약

`SpringDocConfig.java` 삭제 → `back/src/main/kotlin/com/back/global/springDoc/SpringDocConfig.kt` 변환. Swagger/OpenAPI 문서를 "apiV1"(`/api/v1/**`)과 "home"(그 외) 두 그룹으로 나누는 설정.

### 새로운 개념 — 어노테이션 안에 어노테이션을 값으로 넣을 때 `@` 유무

```diff
-@OpenAPIDefinition(info = @Info(title = "API 서버", ...))
+@OpenAPIDefinition(info = Info(title = "API 서버", ...))
```

자바는 어노테이션을 "적용"할 때나 다른 어노테이션의 값으로 "넣을" 때나 항상 `@`를 붙임. 코틀린은 두 상황을 구분: 선언(클래스 등)에 어노테이션을 **적용**할 때만 `@`를 쓰고, 어노테이션 파라미터 자리에 값으로 **생성해 넣을** 때는 그 어노테이션 클래스의 생성자를 호출하는 것과 동일하게 취급해 `@` 없이 씀. 어노테이션 클래스도 결국 필드 몇 개를 가진 특별한 클래스이고, `Info(title = "...")`는 그 클래스의 생성자 호출이라는 점에서 15강 이후 계속 봐온 일반 클래스 생성자 호출과 같은 규칙.

강사 원본은 이 변환 중 그룹 이름을 "home" → "controller"로 바꿨지만, 이유가 불명확한 임의 변경이라 우리는 원래 이름 "home"을 유지.

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

### 질문1
- **Q.** 두 `@Bean`(`groupApiV1`, `groupController`)이 어떻게 동작하는지
- **A.** springdoc이 앱의 모든 엔드포인트를 스캔한 뒤, 각 `GroupedOpenApi` 빈의 필터 규칙(`pathsToMatch`=포함, `pathsToExclude`=제외)에 따라 그룹별로 별도 OpenAPI 문서를 생성. Swagger UI에 그룹 선택 드롭다운이 생겨 "apiV1"(REST API만)과 "home"(HomeController만)을 따로 볼 수 있게 됨. HTML을 반환하는 HomeController를 JSON REST API 문서와 섞이지 않게 분리하는 목적.

### 질문2
- **Q.** "어노테이션 인스턴스를 만들어 넣는다"가 생소함, 구체적으로 설명
- **A.** 어노테이션은 필드 몇 개를 가진 특별한 클래스이고, `@Info(title=...)`는 그 타입의 인스턴스(값들이 채워진 객체)를 만드는 것과 같음. 자바는 적용/값 두 경우 모두 `@`를 쓰지만, 코틀린은 "선언에 적용"할 때만 `@`를 쓰고 "값으로 생성해 넣을" 때는 일반 클래스 생성자 호출과 동일하게 `@` 없이 씀.
