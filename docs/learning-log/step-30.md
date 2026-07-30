# step-30: ServiceException 변환

- 강의 링크: https://www.slog.gg/p/14128#30강
- 상태: 완료

## 요구사항 요약

`ServiceException.java` 삭제 → `back/src/main/kotlin/com/back/global/exception/ServiceException.kt` 변환. `RuntimeException("$resultCode : $msg")`처럼 부모 생성자에 문자열 템플릿 직접 전달(21강 패턴), `getRsData()` → `val rsData get() = ...`(15강 패턴). 19강 결정(`RsData.data: T?`)에 따라 2개 인자 생성자(`@JvmOverloads`) 사용.

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
