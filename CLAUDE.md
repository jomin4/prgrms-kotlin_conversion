# CLAUDE.md

이 저장소에서 작업하는 Claude Code(claude.ai/code)를 위한 안내 문서.

## 프로젝트 개요

Java + Spring Boot로 작성된 게시판/회원 API를 **Kotlin으로 전환**하는 학습 프로젝트. 강의([코프링으로 전환](https://www.slog.gg/p/14128))를 따라가며 한 강의(또는 강의 그룹)씩 자바 코드를 코틀린으로 바꾸고, 그 과정을 `docs/`에 기록한다.

- `back/` — Spring Boot 백엔드. 모든 메인 소스가 Kotlin으로 전환 완료(`src/main/kotlin`), 테스트 일부만 아직 Java(`src/test/java`, 이후 강의에서 전환 예정).
- `front/` — Next.js(React 19, TypeScript) 프런트엔드. 이 프로젝트의 핵심 학습 대상은 아니며 백엔드 API 소비 클라이언트.
- `docs/learning-log/` — 강의별 변환 기록(`step-NN.md`). `PROMPT.md`가 작업 진행 방식(워크플로우 규칙)의 root of truth.
- `docs/conversion-rules/RULES.md` — 강의 기록과 별개로, 재사용 가능한 일반화된 Java→Kotlin 전환 규칙 카탈로그(R-001, R-002, ...).

## 빌드 / 테스트 명령어

### 백엔드 (`back/`)

```bash
./gradlew compileKotlin compileJava compileTestKotlin compileTestJava   # 컴파일만 (빠른 확인)
./gradlew test                                                          # 전체 테스트
```

- 코드를 변경한 뒤에는 커밋 전에 반드시 위 컴파일 명령을 실행해 `BUILD SUCCESSFUL`을 확인한다.
- JPA 관련 변경이나 Repository/Service 계층 변경 시에는 `test`까지 실행해서 런타임 동작(직렬화, 쿼리 결과 등)까지 확인한다.

### 프런트엔드 (`front/`)

```bash
pnpm dev      # 개발 서버
pnpm check    # format + tsc + lint 한번에
```

## 빌드 스택

- Spring Boot 4.1.0, Kotlin 2.2.20 (`kotlin("plugin.spring")`, `kotlin("plugin.jpa")`)
- JDK 25 툴체인이지만 Kotlin이 아직 JVM_25 타겟을 지원하지 않아 `compileJava`/`compileKotlin` 모두 JVM 24로 고정(`build.gradle.kts` 참고)
- Jackson 3.x (`tools.jackson.*` groupId — 구버전 `com.fasterxml.jackson.*`와 혼동 주의)
- DB: H2, 인증: JWT + OAuth2(카카오/구글/네이버) 소셜 로그인

## 코드 구조 (도메인 패키지 구조, `back/src/main/kotlin/com/back/`)

- `domain/member/member/` — 회원(Member): controller / dto / entity / repository / service
- `domain/post/post/`, `domain/post/postComment/` — 게시글 / 댓글
- `domain/home/home/` — 홈 컨트롤러
- `global/` — 공통 설정(`app`), 보안(`security`), 예외 처리(`globalExceptionHandler`, `exception`), 응답 래퍼(`rsData`), AOP(`aspect`), 초기 데이터(`initData`), Rq(요청 컨텍스트 헬퍼)
- `standard/` — 프로젝트 전용 유틸(`util`), 확장 함수(`extensions`)

응답은 공통적으로 `RsData<T>`(결과 코드 + 메시지 + 데이터) 래퍼로 감싸고, 예외는 `ServiceException` + `GlobalExceptionHandler`가 처리한다.

## 이 저장소에서 작업할 때 지켜야 할 것

1. **`docs/learning-log/PROMPT.md`를 먼저 확인**한다 — 강의 진행 방식(어디까지 진행했는지, 어떤 그룹 단위로 진행 중인지, diff만 보여줄지 등)이 여기 정의되어 있다.
2. Java → Kotlin 전환 시 이미 확립된 패턴이 있다면 새로 판단하지 말고 **`docs/conversion-rules/RULES.md`의 기존 규칙을 먼저 따른다.** 새로운 패턴을 발견하면 규칙을 추가한다(번호는 순차 증가, 카테고리/도입 강/패턴/주의사항 포함).
3. `@JvmStatic`/`@JvmOverloads`/`@JvmRecord` 같은 자바 상호운용 어노테이션을 추가하거나 제거할 때는 **반드시 실제 자바 호출부를 grep으로 확인**한 뒤 결정한다(추측 금지, R-065 참고).
4. 변경 후 커밋 전 `back` 디렉터리에서 컴파일(필요시 테스트까지) 통과를 확인한다.
5. 강의 단위(또는 합의된 그룹 단위) 작업이 끝나면 해당 `docs/learning-log/step-NN.md`를 작성/갱신하고, 필요하면 `RULES.md`의 커버리지 라인과 규칙을 갱신한 뒤 커밋한다.
