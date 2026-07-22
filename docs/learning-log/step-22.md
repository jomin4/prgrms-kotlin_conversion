# step-22: CustomUserDetailsService 변환

- 강의 링크: https://www.slog.gg/p/14128#22강
- 상태: 완료

## 요구사항 요약

`CustomUserDetailsService.java` 삭제 → `back/src/main/kotlin/com/back/global/security/CustomUserDetailsService.kt` 변환.

### 새로운 개념

- **후행 람다(trailing lambda)**: `.orElseThrow(() -> new UsernameNotFoundException(...))` → `.orElseThrow { UsernameNotFoundException(...) }`. 함수의 마지막 파라미터가 람다일 때 괄호 밖으로 빼서 `{ }`만 쓸 수 있음. 파라미터 없는 람다는 `() ->` 표시도 불필요.

나머지(`@RequiredArgsConstructor` → 주 생성자, `throws` 제거, 게터 → 프로퍼티 접근)는 기존 패턴(6강, 9강, 15강)의 반복 적용.

`./gradlew compileKotlin compileJava compileTestKotlin compileTestJava` `BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
