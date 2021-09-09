* ## :common 모듈
  * 애플리케이션 모듈이 공통으로 사용할 프로젝트 내부 모듈
  * 애플리케이션 모듈이 할 책임이 있는 것은 절대 여기 두지 않는다.
  * 하위 모듈
    * common
      * api - 요청/응답 base 클래스, 에러 응답 base 클래스
      * exception - Exception 처리, ErrorCode 정의
      * access log - 스프링부트 interceptor, filter, WebMvcConfigurer 이용한 access log
      * aop
        * ControllerAspect - 컨트롤러 별 실행속도 측정
        * RequestInfo - 요청 하나 안에서 동작하는 RequestScope 객체
    * design - 기획 테이블 JSON 파서
      * map 형태로 저장.
      * Custom 하게 파싱위한 클래스 따로 둘 수 있음 - static 으로 직접 사용도 가능