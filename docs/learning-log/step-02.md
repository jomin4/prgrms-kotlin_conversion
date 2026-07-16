# step-02: 기존 프로젝트로부터 새 프로젝트 생성/설정

- 강의 링크: https://www.slog.gg/p/14128#2강
- 상태: 완료

## 요구사항 요약

코드 타이핑 없이, git 명령어의 동작 원리를 이해하는 강의.

```bash
git clone https://github.com/jhs512/p-14184-1 .
rm -rf .git
git init
git remote add origin https://github.com/jhs512/p-14128-2
git add . && git commit -m "002" && git push origin main
```

- `git clone`: 파일 스냅샷 + 원본의 전체 커밋 히스토리까지 함께 복사됨
- `rm -rf .git`: 원본의 커밋 히스토리를 제거 (파일은 유지) — 내 레포에 강사의 커밋이 섞이지 않게 하기 위함
- `git init`: 커밋 0개인 새 저장소로 재시작
- `git remote add origin`: 새 GitHub 레포와 연결
- `add && commit && push`: 로컬 커밋 1개 생성 후 원격 main으로 업로드

우리 프로젝트에서는 원본 `jhs512/p-14184-1` → 새 레포 `jomin4/prgrms-kotlin_conversion`으로 동일하게 적용, `git log --oneline`으로 강사의 커밋 없이 우리 커밋만 남았음을 확인함.

## 아키텍처 다이어그램

해당 없음 (구조 변경 아님, git 저장소 재구성 과정 설명)

## 질문 로그

(없음)
