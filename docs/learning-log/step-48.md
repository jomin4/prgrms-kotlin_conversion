# step-48: 롬복 제거

- 강의 링크: https://www.slog.gg/p/14128#48강
- 상태: 완료

## 요구사항 요약

`back/build.gradle.kts`에서 롬복 의존성(`compileOnly`/`annotationProcessor`/`testCompileOnly`/`testAnnotationProcessor` 4줄) 제거. 이 시점에 메인 소스(`back/src/main/java`)에 자바 파일이 더 이상 하나도 남아있지 않고(9~47강에 걸쳐 전부 코틀린으로 변환 완료), 테스트 코드도 롬복을 쓰지 않아 완전히 제거 가능함을 grep으로 확인.

`./gradlew compileKotlin compileJava` 결과 `compileJava`가 `NO-SOURCE`로 나옴 — 메인 소스에 컴파일할 자바 파일이 아예 없다는 뜻.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
