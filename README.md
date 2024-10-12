# JUPJUP-Back
Lets-JUPJUP 백엔드 레포지토리
# 기술 스택
- Java 11, Spring Boot2, Gradle, Spring Security
- MySQL, Redis, Firebase
- Nginx, Docker
- AWS EC2, AWS RDS, AWS S3, AWS ELB, AWS CodeDeploy
# 핵심 기능
### 1. 사용자 관리
- 카카오 소셜로그인과 Redis 기반 토큰 관리
- 사용자 및 관리자 탈퇴 처리 기능
- 프로필 조회 및 수정 기능

### 2. 플로깅 관련 기능
- 플로깅 모집 게시글 작성 및 필터링 기능
- 참여자 관리 및 목록 조회
- 댓글 및 대댓글 작성/삭제 기능
- 게시글 북마크 기능
- 리뷰 및 좋아요 기능

### 3. 쓰레기통 지도
- 위치 기반 쓰레기통 조회 기능 (1km 반경)
- 사용자 피드백 제공 기능

### 4. 알림 시스템
- 플로깅 성사 여부 실시간 알림
- 댓글 및 대댓글 실시간 알림

### 5. 관리자 기능
- 사용자 신고 내역 조회 및 탈퇴 처리
- 쓰레기통 피드백 조회 기능
# 아키텍처
![image](https://github.com/user-attachments/assets/9a86a640-33d4-43ef-a90f-e489767920eb)

