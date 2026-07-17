# step-16: @JvmStatic 어노테이션

- 강의 링크: https://www.slog.gg/p/14128#16강
- 상태: 완료

## 요구사항 요약

`AppConfig.kt`의 `companion object` 멤버(`isDev`, `isTest`, `isProd`, `isNotProd`)에 `@JvmStatic` 추가.

### 왜 필요한가

`companion object`는 바이트코드 상 진짜 static이 아니라, `Companion`이라는 별도 싱글턴 객체의 **인스턴스 메서드**로 컴파일된다. 그래서 `@JvmStatic` 없이는 자바 코드에서 `AppConfig.Companion.isDev()`처럼 `Companion`을 거쳐야만 호출 가능하다.

`@JvmStatic`을 붙이면 컴파일러가 `Companion` 안의 메서드 외에 **바깥 클래스에 진짜 static 메서드를 하나 더 생성**해서, 자바 코드가 원래(순수 자바 시절)와 똑같이 `AppConfig.isDev()`로 호출할 수 있게 해준다. 코틀린 코드에서의 호출(`AppConfig.isDev`)은 원래도 잘 되므로 영향 없음 — 순전히 자바와의 상호운용성을 위한 장치.

### 이 프로젝트에서의 의미

지금은 `AppConfig`를 참조하는 다른 자바 파일이 없어(grep 확인) 당장 체감되는 효과는 없지만, 자바/코틀린이 혼재된 마이그레이션 중간 단계에서는 이게 없으면 "이 클래스를 코틀린으로 바꿨다"는 이유만으로 그걸 참조하는 모든 자바 파일의 호출부를 찾아 고쳐야 한다. `@JvmStatic`은 그런 연쇄적인 수정을 막아주는 호환성 유지 장치. (32~33강의 `DevInitData`/`NotProdInitData`가 아직 자바 상태로 `AppConfig`를 참조할 가능성 대비해 미리 적용.)

`./gradlew compileKotlin compileJava` `BUILD SUCCESSFUL` 확인.

## 아키텍처 다이어그램

해당 없음 (어노테이션 추가, 구조 변경 아님)

## 질문 로그

### 질문1
- **Q.** 원래 사용 방식과 `@JvmStatic`을 통해 얻는 이점 구체적으로 정리
- **A.** 순수 자바 시절엔 `AppConfig.isDev()`로 직접 호출 가능했음. `companion object`로 바꾸면 실제로는 `Companion`이라는 별도 객체의 인스턴스 메서드로 컴파일되기 때문에, `@JvmStatic` 없이는 자바에서 `AppConfig.Companion.isDev()`로 고쳐 써야 함(호출부 전부 수정 필요). `@JvmStatic`을 붙이면 바깥 클래스에 진짜 static 메서드가 하나 더 생성되어, 자바 호출부를 한 글자도 안 고쳐도 원래처럼 `AppConfig.isDev()`로 계속 컴파일됨. 자바/코틀린 혼재 마이그레이션 중 다른 자바 파일들에 연쇄적인 수정이 퍼지는 걸 막아주는 핵심 장치.
