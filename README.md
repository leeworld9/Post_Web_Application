# Post_Web_Application

## 1. 구현 목표 및 목적
* 소셜 로그인 기능을 지닌 기본적인 게시판 애플리케이션 개발
* **스프링 시큐리티를 이용한 소셜 로그인(OAuth 2.0) 구현**
* **Junit을 이용한 단위 테스트 구현**
* ~~Swagger를 사용한 API 문서 자동화~~ --> **Spring Rest Docs를 사용한 API 문서 자동화**
  * [api-docs.html](https://drive.google.com/file/d/1dO8BbJk6v_-YfN7SNkDrxoFZepeATMh0/view?usp=sharing)
* AWS EC2, AWS RDS, AWS Elastic IP 사용
* **CI/CD를 이용한 배포**
* **무중단 배포환경 구축**

## 2. 구체적인 사용 기술
- 프론트엔드 : mustache 2.6.5
- 백엔드 : Spring Boot 2.6.7
- ORM : JPA
- 인증 : Session 기반
- 테스트 : JUnit 4.13.1
- API 문서 : Spring Rest Docs 2.0.6.RELEASE
- 빌드 도구 : gradle 6.9
- 서버 : AWS
  - Amazone EC2 인스턴스 (Amazon Linux 2)
  - Amazone RDS 인스턴스 (MariaDB)
- CI/CD
  - 테스트/빌드 : Travis CI
  - 빌드 저장소 : AWS S3
  - 배포 : AWS CodeDeploy
- 무중단 배포
  - nginx(latest version)와 profile(real1, real2) 교체를 이용한 무중단 배포

## 3. 추후 해보고 싶은 내용
 - querydsl 사용 
   - 조회용 프레임워크 (복잡한 조건등으로 인해 Entity 클래스만으로 처리하기 어려움)
   - 등록/수정/삭제 등은 SpringDataJpa 사용
 - Spring Security, OAuth 2.0, JWT를 이용한 인증 구현

## 4. 참고
- http://www.yes24.com/Product/Goods/95381086
- https://velog.io/@ehdrms2034/Spring-Application.properties-%ED%95%9C%EA%B8%80%EA%B9%A8%EC%A7%90
- https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/
- https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/set-hostname.html
- https://mchch.tistory.com/223
- https://github.com/jojoldu/freelec-springboot2-webservice/issues/67
- https://github.com/jojoldu/freelec-springboot2-webservice/issues/470
  - 해당 버전은 spring-session은 spring.session.store-type=jdbc 설정값이 제대로 작동하지 않음 --> 최신버전 사용하니 잘 작동함
- https://galid1.tistory.com/385
- https://fun-coding-study.tistory.com/314
- https://yoo11052.tistory.com/113
- https://docs.aws.amazon.com/ko_kr/codedeploy/latest/userguide/deployments-view-logs.html
- https://dev-j.tistory.com/22
- https://developer88.tistory.com/299
- https://huisam.tistory.com/entry/RESTDocs
- https://lannstark.tistory.com/10?category=840828
  - Spring Rest Docs의 documents 작성법이 케이스별로 자세히 나와있음.
- https://velog.io/@hydroniumion/BE2%EC%A3%BC%EC%B0%A8-Spring-Rest-Docs-%EC%A0%81%EC%9A%A9%EA%B8%B0-2
  - Spring Rest Docs은 junit4 와 junit5 가 사용법이 다르니 주의가 필요하다.
- https://dotheright.tistory.com/296
- https://techblog.woowahan.com/2597
- https://docs.spring.io/spring-restdocs/docs/current/reference/html5/
- https://stackoverflow.com/questions/68539790/configuring-asciidoctor-when-using-spring-restdoc
- https://narusas.github.io/2018/03/21/Asciidoc-basic.html#text_formating
