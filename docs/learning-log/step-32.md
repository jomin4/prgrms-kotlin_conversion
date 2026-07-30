# step-32: DevInitData 변환

- 강의 링크: https://www.slog.gg/p/14128#32강
- 상태: 완료

## 요구사항 요약

`DevInitData.java` 삭제 → `back/src/main/kotlin/com/back/global/initData/DevInitData.kt` 변환. dev 프로필에서 OpenAPI 스펙(27강)으로부터 프론트엔드 TypeScript 타입을 자동 생성하는 초기화 로직.

강사 버전은 `import com.back.standard.util.Ut.cmd.runAsync`(함수 하나만 골라 import해서 `runAsync(...)`로 짧게 호출)를 썼지만, 출처가 분명히 보이는 `Ut.cmd.runAsync(...)` 전체 경로 호출을 유지하기로 함(가독성 우선). 강사 버전이 `"typescript@v5"`를 `"typescript"`로 바꾼 것도 채택하지 않고 원래 버전 고정 유지.

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
