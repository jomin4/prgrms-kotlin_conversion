# step-66: 테스트 소스 폴더를 java에서 kotlin으로 이동

- 강의 링크: https://www.slog.gg/p/14128#66강
- 상태: 완료

## 요구사항 요약

60~65강에서 테스트 파일 6개를 전부 `.kt`로 변환해, `back/src/test/java` 아래에는 더 이상 `.java` 파일이 없다. 강사 원본의 66강과 동일하게 `src/test/java` 폴더 전체를 `src/test/kotlin`으로 이동한다(메인 소스는 6강부터 처음부터 `src/main/kotlin`에 둬서 57강에서 이미 선반영됐지만, 테스트 소스는 60강 전까지 자바로 남아있었으므로 이 강에서 실제로 폴더 이동이 발생하는 유일한 지점).

## 작업 내용

```bash
mkdir -p back/src/test/kotlin
cp -r back/src/test/java/* back/src/test/kotlin/
rm -rf back/src/test/java
```

이동 후 `back/src/test/java`는 완전히 사라지고, `back/src/test/kotlin` 아래에 6개 테스트 클래스가 패키지 구조 그대로 위치.

## 최종 검증 (60~66강 그룹 전체)

```
./gradlew compileKotlin compileJava compileTestKotlin compileTestJava --console=plain
→ BUILD SUCCESSFUL

./gradlew test --console=plain
→ BUILD SUCCESSFUL
```

`build/test-results`의 XML 리포트로 각 테스트 클래스의 통과 개수를 확인:

| 테스트 클래스 | 테스트 수 | 실패/에러 |
|---|---|---|
| AuthTokenServiceTest | 4 | 0 |
| ApiV1AdmMemberControllerTest | 4 | 0 |
| ApiV1MemberControllerTest | 7 | 0 |
| ApiV1AdmPostControllerTest | 1 | 0 |
| ApiV1PostControllerTest | 15 | 0 |
| ApiV1PostCommentControllerTest | 7 | 0 |

총 38개 테스트, 전부 통과. 이제 `back/src` 전체(메인+테스트)가 100% 코틀린이다.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
