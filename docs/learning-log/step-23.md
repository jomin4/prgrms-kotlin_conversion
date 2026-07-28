# step-23: CustomOAuth2UserService 변환

- 강의 링크: https://www.slog.gg/p/14128#23강
- 상태: 완료

## 요구사항 요약

`CustomOAuth2UserService.java` 삭제 → `back/src/main/kotlin/com/back/global/security/CustomOAuth2UserService.kt` 변환.

### 강사 커밋에서 제외한 부분

강사의 실제 커밋은 `RsData.kt`도 함께 수정해서 `data: T?` → `data: T`(기본값 `null as T`라는 **안전하지 않은 캐스팅**)로 바꿨음. 이건 코틀린의 null 안정성을 우회하는 위험한 패턴이라 **채택하지 않고 19강의 `data: T?`(nullable) 그대로 유지**하기로 사용자와 협의. 대신 `CustomOAuth2UserService`에서 `.data`를 가져올 때 `!!`로 처리(`modifyOrJoin`이 항상 실제 `Member`로 `RsData`를 구성함을 `MemberService` 코드로 직접 확인 후 적용).

### 새로운 개념

- **문자열 `switch` → `private enum class` + `when`**: 오타에 취약한 문자열 분기 대신, `enum class OAuth2Provider { KAKAO, NAVER, GOOGLE }`로 값의 종류를 못박고 `when`으로 분기 — 분기 하나라도 빠뜨리면 컴파일 타임에 잡힘.
- **`Triple` + 구조분해 할당**: `when` 표현식의 각 분기가 `Triple(a, b, c)`를 반환하고, `val (oauthUserId, nickname, profileImgUrl) = when (...) { ... }`로 한 번에 분해. 자바처럼 빈 문자열로 미리 선언해두고 재할당하는 가변 상태가 없어짐.
- **`Map.getValue(key)` vs `get(key)`**: `get()`은 자바와 동일하게 없으면 `null` 반환(변화 없음). `getValue()`는 코틀린이 추가로 제공하는 별도 함수로, 키가 없으면 즉시 `NoSuchElementException`을 던짐 — "이 키는 반드시 있어야 한다"는 의도를 명확히 할 때 사용.
- **`as Map<String, Any>`**: 타입 캐스팅(검증) 연산자. 값을 변환하는 게 아니라 "이미 그 타입인 것을 안다"고 컴파일러에게 확인시키는 것 — 런타임에 실제로 그 타입이 아니면 `ClassCastException`.
- **`private val logger = LoggerFactory.getLogger(javaClass)`**: `@Slf4j`(Lombok)를 코틀린에서 직접 한 줄로 작성. `javaClass`는 현재 클래스의 `Class` 객체(자바 `getClass()`에 대응).

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인 (unchecked cast 경고 2건은 원본 자바 코드에도 있던 것과 동일 성격이라 정상).

## 아키텍처 다이어그램

해당 없음

## 질문 로그

### 질문1
- **Q.** `attributes.getValue("properties") as Map<String, Any>`에서 `as`의 뜻, 코틀린 `get()`에 null 에러처리가 자바와 다르게 내장돼 있는지
- **A.** `as`는 타입 캐스팅(검증) 연산자 — 값을 변환하지 않고 "이미 그 타입임"을 런타임에 확인만 함(실패 시 `ClassCastException`, 안전 버전은 `as?`로 실패 시 null). `get()` 자체는 자바와 동일(없으면 null, 예외 없음) — 다른 건 코틀린이 추가로 제공하는 **별도 함수** `getValue()`(없으면 `NoSuchElementException`)이지, `get()`의 동작이 바뀐 게 아님.

### 질문2
- **Q.** (이해 확인) 타입 캐스팅은 예상 자료형이 맞으면 그대로, 아니면 에러를 내는 안전장치가 맞는지
- **A.** 맞음, 다만 "형변환"이 아니라 "검증"에 가까움 — 데이터를 바꾸는 게 아니라 이미 그 타입인지 런타임에 확인만 하는 것. 가정이 틀렸을 때 조용히 잘못된 값으로 진행하는 대신 그 자리에서 명확한 예외로 알려준다는 의미의 안전장치.
