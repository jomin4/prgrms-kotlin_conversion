# step-04: 스프링부트 3.x에서 지원하는 코틀린 버전과 JDK 24 제약

- 강의 링크: https://www.slog.gg/p/14128#4강
- 상태: 완료

## 요구사항 요약

개념 확인 강의 (코드 타이핑 없음).

- Spring Boot 3.x는 Kotlin 1.9.x까지만 공식 지원
- Kotlin 1.9.x는 JDK 24 툴체인 미지원
- Spring Boot 4.x부터는 최신 Kotlin(2.x)을 지원하여 JDK 24/25도 사용 가능

우리 프로젝트(`back/build.gradle.kts`) 확인 결과: Spring Boot `4.1.0` + JDK `25` 조합이라 이 버전 제약에서 자유로움 (강의 녹화 시점보다 최신 스택 사용 중).

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
