# step-31: GlobalExceptionHandler 변환

- 강의 링크: https://www.slog.gg/p/14128#31강
- 상태: 완료

## 요구사항 요약

`GlobalExceptionHandler.java` 삭제 → `back/src/main/kotlin/com/back/global/globalExceptionHandler/GlobalExceptionHandler.kt` 변환. 5개 예외 핸들러(`NoSuchElementException`, `ConstraintViolationException`, `MethodArgumentNotValidException`, `HttpMessageNotReadableException`, `ServiceException`)를 각각 `RsData` JSON으로 응답.

강사 버전은 `@RestControllerAdvice` → `@ControllerAdvice`로 바꿨으나, "이 클래스는 항상 JSON을 반환한다"는 의도를 명확히 하기 위해 원래대로 `@RestControllerAdvice` 유지.

### 새로운 개념

- **`.getOrElse(1) { path }` / `.getOrNull(...) ?: "Unknown"`**: 자바 원본은 `split(...)[1]`로 인덱스를 무조건 꺼내 `.`이 없는 경로에서 `ArrayIndexOutOfBoundsException` 위험이 있었음. 코틀린은 범위를 벗어나면 예외 대신 기본값을 쓰도록 안전장치 추가(원본에 없던 방어 로직).
- **`.filterIsInstance<FieldError>()`**: `stream().filter(x instanceof T).map(x -> (T) x)`를 한 함수로 압축(18강 `is` 스마트캐스트의 컬렉션 버전).
- **`ResponseEntity.status(...).body(...)`**: `new ResponseEntity<>(body, status)` 생성자 호출을 빌더 스타일로.
- 같은 이름 `handle`로 여러 오버로드(파라미터 타입만 다름) — 코틀린도 자바와 동일하게 오버로딩 지원.

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
