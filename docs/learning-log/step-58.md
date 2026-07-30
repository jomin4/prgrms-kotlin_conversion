# step-58: @JvmStatic, @JvmOverloads, @JvmRecord 중 필요없는 것 제거

- 강의 링크: https://www.slog.gg/p/14128#58강
- 상태: 완료

## 요구사항 요약

16강(R-018)/19강(R-029)/17강(R-020) 등에서 "아직 자바로 남은 파일들이 이걸 필요로 할 수도 있다"는 이유로 미리 넣어뒀던 자바 상호운용 어노테이션들을, 이제 메인 소스가 전부 코틀린으로 바뀌고 테스트만 자바로 남은 시점에 **실제로 grep해서 확인 후** 필요 없는 것만 제거.

### 확인 방법과 결과

```bash
grep -rln "AppConfig" back/src/test --include="*.java"          # 결과 없음 → @JvmStatic 불필요
grep -rn "Ut\.json\.toString" back/src --include="*.kt" "*.java" # 코틀린 호출뿐, 항상 1개 인자 → @JvmStatic/@JvmOverloads 불필요
grep -rn "Ut\.cmd\.runAsync" back/src --include="*.kt" "*.java"  # 코틀린 호출뿐 → @JvmStatic 불필요
grep -rn "NotProdMember" back/src --include="*.kt" "*.java"      # 코틀린 호출뿐, 생성자 호출만 → @JvmRecord 불필요
grep -rn "RsData" back/src/test --include="*.java"               # 자바에서 전혀 참조 안 함 → @JvmRecord/@JvmOverloads 불필요
grep -rn "Ut\.jwt\." back/src/test --include="*.java"            # AuthTokenServiceTest.java에서 직접 호출 → @JvmStatic **유지 필요**
```

### 제거한 것

| 파일 | 제거 |
|---|---|
| `AppConfig.kt` | `isDev`/`isTest`/`isProd`/`isNotProd`에서 `@JvmStatic` 4곳 |
| `CustomConfigProperties.kt` | `NotProdMember`에서 `@JvmRecord` |
| `RsData.kt` | 클래스에서 `@JvmRecord`, 보조 생성자에서 `@JvmOverloads` |
| `Ut.kt` | `json.toString`에서 `@JvmStatic`+`@JvmOverloads`, `cmd.runAsync`에서 `@JvmStatic` |

### 유지한 것

`Ut.jwt.toString`/`isValid`/`payload`의 `@JvmStatic`은 `AuthTokenServiceTest.java`(아직 자바, 60강에서 변환 예정)가 직접 호출하므로 그대로 유지.

### 검증 (중요)

`RsData`에서 `@JvmRecord`를 제거하면 접근자가 record 스타일(`statusCode()`)에서 평범한 JavaBean 게터(`getStatusCode()`)로 바뀌는데, `@field:JsonIgnore`가 여전히 유효한지가 관건이었음. `./gradlew test`로 **전체 테스트(MockMvc 기반 JSON 응답 검증 포함)를 실제로 실행**해서 정상 통과함을 확인 — Jackson은 논리적 프로퍼티 이름 기준으로 필드/게터/생성자 파라미터의 어노테이션을 병합해서 인식하므로, record 여부와 무관하게 `@field:JsonIgnore`가 계속 작동함.

`BUILD SUCCESSFUL`, 전체 테스트 통과 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
