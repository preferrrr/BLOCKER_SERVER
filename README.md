<div align="center" >

# BLOCKER

</div>


<div align="center" >
</br>
BLOCKER는 비대면 전자 계약 플랫폼으로, 프라이빗 블록체인(하이퍼레저 페브릭)을 사용하여 동업 등 다양한 계약을 위한 신뢰적인 공간을 제공하고 계약서의 위변조로 인한 피해를 방지하고자 제작했습니다.

</br>


[BLOCKER 바로가기](https://blockerrrr.site)

</div>

</br>

## 화면
</br>


## 기술 스택
- Springboot 3.1.1
- Spring Security
    - jwt
- Spring Data JPA
- QueryDSL
- JUnit5
- Kafka
- WebSocket (stomp)
- DB
    - MongoDB
    - MySql
- AWS
    - EC2
    - RDS
    - S3
- NGINX
    - load balancing
    - https

</br>

## Goal

- 계약서에 참여자들이 모두 서명하면 계약이 체결됩니다.
- 체결된 계약서를 파기할 수 있습니다.
- ios와 웹에서 모두 사용하도록 합니다.
- 테스트 커버리지 80%를 목표로 테스트 코드를 작성합니다.
- 실시간 단체 채팅을 구현합니다.
- 이미지를 클라우드 서버에 저장합니다.
- 서버를 로드밸런싱 합니다.
- AWS 배포 후 도메인과 https를 적용합니다.

</br>

## Challenge

- 계약서의 상태를 미체결, 진행 중, 체결, 파기 4가지로 분류
  - enum으로 계약서의 상태를 구분
- 체결된 계약서를 파기하면 파기 계약서가 생성되고 파기 계약서에 참여자들이 모두 서명하면, 기존의 계약서의 상태가 파기로 바뀌도록 구현
- ios와 웹 모두 같은 인가 방식을 사용하기 위해 **JWT**로 선택
  - ios에서 웹 뷰를 쓰는 것이 아니므로 세션 방식 사용이 어려움
  - 또한 **JWT를 사용하는 것이 확장성 측면에서 유리**하다고 판단
- 모든 비즈니스 로직의 단위 테스트 코드를 작성하여 **라인 커버리지 80프로**까지 작성
  - Mocking 하지 않고 실제 데이터를 사용한 테스트 중 fetch join에서 문제 발생
    - 트랜잭션 중첩과 JPA의 1차 캐시에 의한 것이었고,
      **@AfterEach**로 DB를 리셋하여 문제 해결
    - 테스트 또한 유지 보수의 대상과 비용이기 때문에, 성능을 고려하여 **deleteAllInBatch()**로 리셋
    - [**https://prefercoding.tistory.com/42**](https://prefercoding.tistory.com/42)
- 게시글의 이미지 저장을 **AWS S3**에 저장
  - 응답 시간이 오래 걸리는 문제가 있음
  - github의 issue에서 이미지를 업로드하는 것과 같은 방식으로, 이미지 업로드 api를 따로 만들어 게시글 저장과 따로 처리함
- 서버에 docker를 사용해서 스프링 인스턴스 2개를 실행하고, **nginx의 리버스 프록시를 사용하여 로드 밸런싱**
- 실시간 단체 채팅 기능을 구현하기 위해 **STOMP** 프로토콜 사용
  - 종종 채팅이 전달되지 않는 문제 발견
    - 스프링 인스턴스들은 nginx로 로드 밸런싱 되고 있으며,
      STOMP의 pub/sub 구조에서 **구독 정보는 각 인스턴스끼리 공유되지 않기 때문**
  - **Kafka**를 사용하여 메시지를 Kafka Topic으로 보내고, 각 인스턴스의 group id를 다르게 사용하여 해결
- 채팅 메시지를 영구 저장하기 위해 **MongoDB** 사용
  - 작성한 메시지는 수정과 삭제가 이루어지지 않고, 저장과 조회만 발생
  - 스키마가 정해지지 않기 때문에 다양한 형식의 메시지를 유연하게 저장 가능
  - 채팅 데이터는 사용자와 비례하여 대용량의 데이터가 쌓일 것이므로 MySQL보다 비교적 수평적 확장이 쉬움
- 가비아에서 도메인 구입 후 AWS의 Route 53으로 도메인을 적용
- nginx와 certbot을 사용하여 https 적용

</br>


## 아키텍처 구조
![아키텍쳐](https://github.com/preferrrr/BLOCKER_SERVER/assets/99793526/6102bef9-f0f8-4103-9c68-6b39d668ea92)

## Rest API 명세서
- [API 명세서 구글시트](https://docs.google.com/spreadsheets/d/1DFMd0ERGCjn0O0FpOp1oUvjnTeKOAhN43ziPQ3hMcdg/edit#gid=2006977463) 

![API 명세서](https://github.com/preferrrr/BLOCKER_SERVER/assets/99793526/c4731807-afd7-4ecf-8ec2-bd66e6b6cb54)

</br>

## ERD
![ERD](https://github.com/preferrrr/BLOCKER_SERVER/assets/99793526/4c8d2f64-2790-4d21-bf3b-082db1aa87ea) 

</br>


## Team
<div align="center" >


 
|이선호|조윤찬|오예준|
|:---:|:---:|:---:|
|<img width="230px" src="https://avatars.githubusercontent.com/u/99793526?v=4"/> | <img width="230px" src="https://avatars.githubusercontent.com/u/87313979?v=4"/> |<img width="230px" src="https://avatars.githubusercontent.com/u/101854418?v=4"/> |
|[@preferrrr](https://github.com/preferrrr)|[@YOON-CC](https://github.com/YOON-CC)|[@nu-jey](https://github.com/ddogong)|
|Backend| Frontend | ios |

</div>

</br>
