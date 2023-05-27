
# 매칭 플랫폼
- 프로젝트명 : 핸드 오버(노인배웅, 아이돌봄, 동물돌봄과 같이 케어 서비스를 필요로 하는 사용자들과 케어시터들을 매칭시켜주는 플랫폼)
- 개발 기간 : 2023/3 ~ 2023/6


# 프로젝트 소개

- 케어시터는 여유 시간에 돈을 벌면서 누군가에게 도움을 줄 수 있고, 원하는 시간, 업무를 찾을 수 있습니다.
- 매칭하기 버튼을 클릭할 시 매칭글 작성자는 케어시터와 실시간 채팅을 통해 일정을 조율하여 원하는 업무를 부탁할 수 있는 시스템입니다.
- 더불어 자유게시판, 쪽지, 매칭글 찜, 신고, 관리자, 게시판 댓글 서비스를 이용할 수 있습니다. 

# 사용 기술
- Java 17
- Spring data JPA
- Spring boot 3.0.4
- Spring Security
- Spring Web
- Jwt
- OAuth2 Client
- Validation
- Lombok
- Mysql 8.0.32
- Swagger 
- Junit5
- AWS Lightsail, EC2
- AWS Route 53 
# 팀원

## Backend
- [김승범](https://github.com/daily1313) 
- [김태수](https://github.com/kimtaesoo99)
- [오정환](https://github.com/poll9999)

<hr>

## Frontend
- [박은지](https://github.com/eunji0)
- [이나령](https://github.com/devryyeong)

<hr>

## DB ERD 
MySQLWorkbench tool을 이용하여 ERD 추출
<img width="500" alt="스크린샷 2023-05-28 오전 3 54 47" src="https://github.com/daily1313/hand-over-backend/assets/88074556/78391744-7893-45b6-97a8-36403599f4a0">



## Deploy 

```text
1. EC2 서버 접속 (ssh -i (pem location) ubuntu@(public ip Address))
2. git clone https://github.com/daily1313/hand-over-backend.git (배포할 프로젝트 복제)
3. cd hand-over-backend (해당 디렉토리로 이동)
4. ./gradlew build (재빌드시 ./gradlew clean build)
5. nohup java -jar build/libs/[jar파일명].jar & (백그라운드 배포) 
  
```

## Git Convention

```text
feat : 기능추가
fix : 버그 수정
refactor : 리팩토링, 기능은 그대로 두고 코드를 수정
style : formatting, 세미콜론 추가 / 코드 변경은 없음
chore : 라이브러리 설치, 빌드 작업 업데이트
docs : 주석 추가 삭제, 문서 변경
```
<hr>







