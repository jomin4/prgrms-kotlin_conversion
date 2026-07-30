# step-29: ResponseAspect 변환

- 강의 링크: https://www.slog.gg/p/14128#29강
- 상태: 완료

## 요구사항 요약

`ResponseAspect.java` 삭제 → `back/src/main/kotlin/com/back/global/aspect/ResponseAspect.kt` 변환. 컨트롤러가 `RsData`를 반환할 때마다 AOP로 가로채 HTTP 상태 코드를 자동 설정하는 클래스.

반환 타입을 `Object`(자바) 대신 `RsData<*>`로 명확히 지정(코틀린 스타 프로젝션, 자바 `RsData<?>`에 대응). 강사 버전은 메서드 이름을 `handleResponseStrict`로 바꿨으나 이유가 불명확해 원래 이름 `handleResponse` 유지.

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
