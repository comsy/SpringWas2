:core 모듈
=============
  * 거의 모든 모듈이 가지게 될 코어 라이브러리
  * 다른 프로젝트에도 들어 갈 만한 진짜 코어만 여기 둔다.
  * 최대한 사용하지 않는다 => :common or :app 모듈에 들어갈 녀석이 아닌지 고민해본다.
  * 하위 모듈
    * core
      * DateFormatConfig - JSON 파싱 시 LocalDateTime 의 포맷을 정의
    * cache-redis
      * REDIS, 스프링캐시를 이용한 캐시매니저
    * mapper
      * Object - DTO 변환용