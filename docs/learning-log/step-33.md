# step-33: NotProdInitData 변환

- 강의 링크: https://www.slog.gg/p/14128#33강
- 상태: 완료

## 요구사항 요약

`NotProdInitData.java` 삭제 → `back/src/main/kotlin/com/back/global/initData/NotProdInitData.kt` 변환. dev/test 환경 시작 시 더미 회원/게시글/댓글을 자동 생성하는 초기화 로직.

### 새로운 개념 — self-injection 패턴

```kotlin
@Lazy
@Autowired
private lateinit var self: NotProdInitData
```

`work1()`/`work2()`에 `@Transactional`이 붙어있는데, Spring의 `@Transactional`은 **프록시를 거쳐 호출될 때만** 동작함. 클래스 내부에서 `this.work1()`(또는 그냥 `work1()`)을 직접 호출하면 프록시를 우회해서 트랜잭션이 적용 안 됨. 그래서 Spring이 관리하는 프록시 버전의 자기 자신(`self`)을 별도로 주입받아 `self.work1()`처럼 프록시를 거쳐 호출하는 잘 알려진 우회 패턴. `@Lazy`는 자기 자신을 주입받다가 생기는 순환참조 문제를 늦은 초기화로 회피. 생성자 주입이 아니라 `lateinit var` + 필드 주입을 쓴 이유는 "일반 의존성이 아니라 특수한 우회 목적"이기 때문(21강 생성자 주입 원칙의 예외 사례).

`BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음

## 질문 로그

(없음)
