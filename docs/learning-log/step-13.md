# step-13: PostDto, PostWithContentDto에서 상속제거하여 소스코드 단순화

- 강의 링크: https://www.slog.gg/p/14128#13강
- 상태: 완료 (12강에서 선반영됨, 추가 작업 없음)

## 요구사항 요약

강사 원본에서는 `PostWithContentDto extends PostDto` 상속 구조를 없애고 `data class`로 바꾸는 강. 우리 프로젝트는 베이스가 이미 두 DTO를 독립된 record로 갖고 있었고, [12강](step-12.md)에서 바로 상속 없는 `data class` 두 개로 변환했기 때문에 이 강에서 추가로 할 작업이 없다.

강사의 "data class로 변경한 이유" 노트 요약(참고용):
- 사실 변경하지 않아도 된다 (일반 class로 유지해도 동작함)
- 상속을 포기했기 때문에 data class로 바꾸는 게 가능해졌다 (data class는 다른 클래스를 상속할 수 없다는 코틀린 제약)
- DTO처럼 값을 담기만 하는 클래스는 data class를 안 쓸 이유가 없어서 변경

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
