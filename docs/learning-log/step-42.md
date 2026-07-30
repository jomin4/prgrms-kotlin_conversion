# step-42: PostRepository, MemberRepository 변환

- 강의 링크: https://www.slog.gg/p/14128#42강
- 상태: 완료

## 요구사항 요약

`PostRepository.java`/`MemberRepository.java` 삭제 → 각각 `.kt`로 변환.

### 새로운 개념

- **인터페이스 상속에도 `:`**: 클래스 상속(21강)뿐 아니라 인터페이스가 다른 인터페이스를 상속(`extends`)할 때도 코틀린은 `:`를 씀 — `interface PostRepository : JpaRepository<Post, Int>`.
- **`Integer` → `Int`**: 자바 제네릭에 박싱 타입(`Integer`)을 써야 했던 자리(`JpaRepository<Post, Integer>`)에, 코틀린은 원시/박싱 구분 없는 `Int` 하나만 쓰면 됨 — 컴파일러가 필요에 따라 자동으로 박싱 처리.

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
