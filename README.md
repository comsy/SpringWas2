RED스튜디오 serverWas
=============

* 모듈
  > 의존성 관리, 계층화된 모듈 레이어로 분리
  * base 모듈
    * :core
      * 모든 모듈이 가지게 될 코어 라이브러리
        * 다른 프로젝트에도 들어 갈 만한 진짜 코어만 여기 둔다.
        * 최대한 사용하지 않는다.
    * :common
      * 애플리케이션 모듈이 공통으로 사용할 프로젝트 내부 모듈
      * 애플리케이션 모듈이 할 책임이 있는 것은 절대 여기 두지 않는다. 
    * :domain
      * 도메인 관련된 것들을 도메인 별로 분류해서 구성한다.
      * Entity, Repository, Dto, Dao(Repository+Cache)
  * 애플리케이션 모듈
    * :game
      * 게임 API WAS 서버
    * :admin
      * 서버 모니터링

* 참조
   * 멀티모듈 설계 - [멀티모듈 설계 이야기 with Spring, Gradle](https://techblog.woowahan.com/2637/)
   * 