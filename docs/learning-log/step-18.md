# step-18: Rq 변환

- 강의 링크: https://www.slog.gg/p/14128#18강
- 상태: 완료

## 요구사항 요약

`Rq.java` 삭제 → `back/src/main/kotlin/com/back/global/rq/Rq.kt` 변환. 함께 `SecurityUser.java`의 `@Getter` 제거 + `getId()`/`getNickname()` 직접 구현(R-012 패턴, `Rq.kt`가 `it.id`/`it.nickname`으로 접근하기 위해 필요).

### 새로운 개념 (자바 → 코틀린)

- **Optional 체인 → `?.`(안전 호출) + `?:`(엘비스 연산자)**: `Optional.ofNullable(x).map().filter().orElse(default)` 패턴을 `x?.let{}?.filter{} ?: default` 형태로 압축.
- **`instanceof` + 캐스팅 → `is` 스마트 캐스트**: `if (x instanceof T) { T t = (T) x; ... }`가 `if (x is T) { x.멤버 }`로 — `is` 확인 블록 안에서는 캐스팅 없이 바로 그 타입처럼 접근 가능.
- **`.let { }`**: 값이 null이 아닐 때만 블록을 실행해서 결과를 반환하는 스코프 함수.
- **`.apply { }`**: 객체를 만들면서 그 자리에서 여러 속성을 설정(각 줄마다 `cookie.setXxx()` 반복 대신, 블록 안에서 `this` 생략하고 바로 프로퍼티 대입).
- **`firstOrNull { }`, `takeIf { }`**: `stream().filter().findFirst().orElse(null)` 같은 자바 스트림 관용구를 대체하는 컬렉션/조건 처리 함수.
- **`val actor: Member get() = ...`**: 프로퍼티의 커스텀 게터 — 저장된 값이 아니라 읽을 때마다 재계산되는 "표현식 게터"(15강 `isDev`와 동일 패턴). `fun getActor(): Member`로 써도 같은 바이트코드가 나오지만, 원본이 JavaBean 게터였으므로 프로퍼티로 옮기는 게 관례.

### 확인 후 적용한 동작 변경 (강사 버전 채택)

1. **`getActor()` → `actor`**: 원본은 인증 안 됐으면 `null` 반환. 강사 버전은 `IllegalStateException`을 던지도록 변경. 우리 코드베이스에서 `rq.getActor()` 호출부(`ApiV1PostController`, `ApiV1PostCommentController`)가 전부 null 체크 없이 바로 쓰고 있어(이미 인증 필수 경로) 안전하다고 판단, 강사 버전 채택.
2. **`setHeader`의 공백 처리 분기 삭제**: 원본은 값이 공백이면 `req.removeAttribute`, 있으면 `resp.setHeader`. 강사 버전은 분기 없이 `resp.setHeader`만 남김. 유일한 호출부(`CustomAuthenticationFilter`)가 항상 실제 토큰 값을 넘겨 영향 없음 확인 후 채택.

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인 — 기존 자바 호출부(`ApiV1PostController` 등) 무수정으로 정상 컴파일.

## 진행 방식 개선

- **18강부터 확정**: diff 전에 자바 원본을 한 줄씩 해석하는 단계 추가 (`PROMPT.md`에 이미 반영, 실제로 이번 강에서 처음 적용).
- 동작이 바뀌는 부분(단순 문법 변환이 아닌 경우)은 적용 전에 실제 호출부를 grep으로 확인하고 사용자에게 확인받은 뒤 진행하는 것을 원칙으로 함.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

### 질문1
- **Q.** `get()`으로 쓰는 게 새로 함수를 만드는 건지, 기존 걸 연결하는 건지
- **A.** 새로 만드는 것. `val actor: Member get() = ...`는 저장 공간(backing field) 없이, 프로퍼티를 읽을 때마다 재계산되는 게터를 직접 정의하는 것. `get() = 표현식`은 코틀린의 "단일 표현식 함수" 축약 문법(`fun f() = 표현식`)을 게터에 적용한 형태. `fun getActor(): Member`로 써도 바이트코드는 비슷하지만, 호출부 문법(괄호 유무)과 관례(원본이 JavaBean 게터였다면 프로퍼티로 옮기는 게 자연스러움)가 다름.
