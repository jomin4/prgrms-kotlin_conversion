# step-28: CustomConfigProperties 변환

- 강의 링크: https://www.slog.gg/p/14128#28강
- 상태: 완료

## 요구사항 요약

`CustomConfigProperties.java` 삭제 → `back/src/main/kotlin/com/back/global/app/CustomConfigProperties.kt` 변환. `application.yml`의 `custom.not-prod-members`를 바인딩하는 `@ConfigurationProperties` 클래스.

`lateinit var notProdMembers: MutableList<NotProdMember>` — Spring이 나중에 값을 채워 넣으므로 `lateinit`(15강 패턴), 세터가 필요해 `var`. 중첩 `NotProdMember`는 `@JvmRecord data class`(19강 R-029) — 33강 `NotProdInitData`가 `.username()` 등 record 스타일 접근을 쓸 가능성 대비.

`BUILD SUCCESSFUL` 확인 (28~33강 그룹 전체 빌드로 검증).

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
