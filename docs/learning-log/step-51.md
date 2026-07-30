# step-51: 확장 함수로 Base64 관련 코드 간소화

- 강의 링크: https://www.slog.gg/p/14128#51강
- 상태: 완료

## 요구사항 요약

25강(`CustomOAuth2AuthorizationRequestResolver`)과 24강(`CustomOAuth2LoginSuccessHandler`)에 중복되던 Base64 URL-Safe 인코딩/디코딩 로직을 `back/src/main/kotlin/com/back/standard/extensions/base64Encode.kt`의 확장 함수로 추출.

```kotlin
@OptIn(ExperimentalEncodingApi::class)
fun String.base64Encode(): String {
    return Base64.UrlSafe.encode(this.toByteArray(StandardCharsets.UTF_8))
}

@OptIn(ExperimentalEncodingApi::class)
fun String.base64Decode(): String {
    return Base64.UrlSafe.decode(this).decodeToString()
}
```

```diff
-val encodedState = Base64.getUrlEncoder().encodeToString(rawState.toByteArray(StandardCharsets.UTF_8))
+val encodedState = rawState.base64Encode()
```
```diff
-val decoded = Base64.getUrlDecoder().decode(encoded)
-String(decoded, StandardCharsets.UTF_8)
+encoded.base64Decode()
```

### 새로운 개념

- **`String`에 확장 함수 추가**: `fun String.base64Encode(): String { ... }`처럼, 이미 존재하는 클래스(자바 표준 라이브러리의 `String`도 포함)에 마치 원래 멤버인 것처럼 새 함수를 추가하는 코틀린 문법. 호출부는 `rawState.base64Encode()`처럼 마치 `String`이 원래 그 메서드를 가진 것처럼 자연스럽게 씀 (9강 `bufferedReader()` 등 표준 라이브러리 확장 함수를 우리가 직접 만드는 버전).
- **`kotlin.io.encoding.Base64`**: 코틀린 표준 라이브러리에 새로 추가된 Base64 API(자바의 `java.util.Base64`를 대체). 아직 실험적(`@OptIn(ExperimentalEncodingApi::class)` 필요)이라 명시적 옵트인이 필요.

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
