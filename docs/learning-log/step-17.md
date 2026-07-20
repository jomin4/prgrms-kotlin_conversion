# step-17: Ut 변환

- 강의 링크: https://www.slog.gg/p/14128#17강
- 상태: 완료

## 요구사항 요약

`Ut.java`(nested static class `jwt`/`json`/`cmd`) 삭제 → `back/src/main/kotlin/com/back/standard/util/Ut.kt`로 변환.

### 사전 조사: 자바 호출부 확인 (16강의 원칙 실전 적용)

`@JvmStatic`을 어디에 붙일지 grep으로 실제 자바 호출부를 확인:

| 함수 | 자바 호출부 | `@JvmStatic` 필요? |
|---|---|---|
| `jwt.toString`, `jwt.isValid`, `jwt.payload` | `AuthTokenService.java`, `AuthTokenServiceTest.java` | 필요 |
| `json.toString` (1개 인자) | `CustomAuthenticationFilter.java` | 필요 (+`@JvmOverloads`) |
| `json.toString` (2개 인자) | `SecurityConfig.java` | 필요 |
| `cmd.runAsync` | `DevInitData.java` | 필요 |
| `cmd.run` | 없음 (내부에서만 호출) | 불필요 |

### 새로운 개념

1. **`class` → `object`**: static 멤버만 있는 유틸 클래스는 `object` 선언 하나로 대체. 컴파일되면 자바의 `public static final X INSTANCE = new X()` 싱글턴 패턴과 동일. `object`의 멤버도 바이트코드상 `INSTANCE`의 **인스턴스 메서드**라서, 자바에서 호출하려면 15~16강과 동일하게 `@JvmStatic`이 필요함.
2. **`@JvmOverloads`**: 코틀린 디폴트 파라미터(`defaultValue: String? = null`)는 자바가 이해 못 함 — 없으면 자바 바이트코드엔 풀파라미터 메서드 하나만 생겨서, 인자를 생략한 자바 호출(`Ut.json.toString(rsData)`)이 컴파일 에러. `@JvmOverloads`를 붙이면 파라미터 개수별 오버로드 메서드가 자동 생성됨.
3. **`vararg`**: 자바 `String... args`와 1:1 대응. 선언은 그대로 대응되지만, 이미 배열로 있는 값을 vararg 자리에 넘길 땐 스프레드 연산자(`*args`)로 풀어서 전달해야 함.
4. **`.lowercase(Locale.getDefault())`**: 코틀린은 인자 없는 `toLowerCase()`를 지원하지 않음(터키어 로케일 버그 등 로케일 의존성 문제 방지). 로케일 명시를 강제. 원본과 동일한 동작을 위해 `Locale.getDefault()` 사용.
5. **`InputStream.bufferedReader().useLines { }`**: 자바의 `BufferedReader` + `while(readLine())` + try-with-resources를 코틀린 확장 함수 체인으로 압축. `useLines`는 자동으로 리소스를 닫아줌(`Closeable.use`와 같은 원리).

우리 프로젝트는 JJWT API가 강사 원본과 달라서(`.parseSignedClaims()` 사용) 그 부분은 원본 로직을 그대로 유지.

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인 — 기존 자바 호출부(`AuthTokenService`, `DevInitData`, `CustomAuthenticationFilter`, `SecurityConfig`, 테스트) 전부 무수정으로 정상 컴파일됨.

## 진행 방식 개선

- 새로운 개념 설명 시 관련 자바/코틀린 코드 조각을 나란히 보여주는 방식을 확정 (17강에서 처음 적용, `PROMPT.md`에 반영).
- **18강부터**: diff를 보여주기 전에 먼저 자바 원본 코드를 한 줄씩 해석하며 각 줄의 역할을 설명하는 단계를 추가하기로 함(`PROMPT.md`에 반영) — 자바 코드 자체를 이해하지 못한 채 변환만 보면 혼란스럽다는 피드백 반영.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

### 질문1
- **Q.** `INSTANCE`라는 객체의 인스턴스 메서드, 구체적으로 설명
- **A.** `object json`은 자바로 `public static final json INSTANCE = new json(); private json(){}` + `toString`은 그 `INSTANCE`의 인스턴스 메서드로 컴파일됨. 클래스 로딩 시 `INSTANCE`가 딱 한 번 생성됨. `@JvmStatic` 없이 자바에서 호출하려면 `Ut.json.INSTANCE.toString(x)`처럼 인스턴스를 거쳐야 함. 코틀린 코드는 컴파일러가 `.INSTANCE` 접근을 자동으로 끼워넣어주므로 이 사실이 안 보임.

### 질문2
- **Q.** `run(*args)` 배열 언패킹이 실제 값으로 어떻게 되는지
- **A.** `runAsync("git", "status")` 호출 시 `args`(runAsync 내부)는 `["git", "status"]` 배열. `run(*args)`에서 `*`가 배열을 낱개로 풀어 `run("git", "status")` 호출과 동일하게 만듦. `run` 내부에서 `vararg args: String`가 다시 배열로 조립되어 `args`(run 내부, 별개 변수) = `["git", "status"]`. `*` 없이 `run(args)`라고 쓰면 "배열 하나를 String 자리에 넣으려는" 타입 불일치로 컴파일 에러.

### 질문3
- **Q.** 로케일 안전성 코드가 뭐고 왜 필요한지
- **A.** 터키어 로케일에서는 `"I".lowercase()`가 `"i"`가 아니라 `"ı"`(점 없는 i)가 되는 등, 문자 대소문자 변환 규칙이 언어별로 다름. 자바의 인자 없는 `toLowerCase()`는 시스템 기본 로케일을 몰래 사용해 환경별로 다르게 동작할 위험이 있음. 코틀린은 인자 없는 버전을 아예 없애고 로케일 명시를 강제. `Locale.getDefault()`는 원본 자바 코드의 동작(시스템 기본 로케일 사용)을 그대로 유지하기 위해 선택.

### 질문4
- **Q.** 자바 스트림/IO → 코틀린 확장 함수 부분 구체적으로
- **A.** 자바의 `new BufferedReader(new InputStreamReader(stream))` + `while(readLine() != null)` + try-with-resources를, 코틀린은 `InputStream`에 추가된 확장 함수(`bufferedReader()`, `useLines { }`)로 압축. `useLines`는 한 줄씩 지연 평가하는 `Sequence<String>`을 넘겨주고 블록이 끝나면 자동으로 리더를 닫아줌(try-with-resources와 동일 원리).
