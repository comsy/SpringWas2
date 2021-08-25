package nr.was.global.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.domain.EntityMaster;
import nr.was.global.util.cache.CacheManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheSyncAspect {

    private final CacheManager<? extends EntityMaster> cacheManager;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Before("execution(* *..*.*Service.*(..))")
    public void prepareCache( JoinPoint joinPoint){
        log.debug("[CacheSyncAspect]prepareCache");
        applicationEventPublisher.publishEvent(joinPoint);
    }

//    @AfterReturning("execution(* *..*.*Service.*(..))")
//    public void syncCache( JoinPoint joinPoint){
//        log.debug("[CacheSyncAspect]syncCache -캐시 싱크");
//        cacheManager.syncAll();
//    }

    @AfterThrowing("execution(* *..*.*Service.*(..))")
    public void rollbackCache( JoinPoint joinPoint){
        log.debug("[CacheSyncAspect]rollbackCache - 캐시 롤백");
        cacheManager.rollbackAll();
    }

    // readOnly 트랜잭션 에서는 동작하지 않음.
//    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
//    public Object checkTransactional(ProceedingJoinPoint joinPoint) throws Throwable {
//        String serviceName = joinPoint.getTarget().getClass().getSimpleName();
//        log.debug("---------------------------[CacheSyncAspect]트랜잭션 시작 : " + serviceName);
//        Object result = joinPoint.proceed();
//        log.debug("---------------------------[CacheSyncAspect]트랜잭션 종료" + serviceName);
//
//        return result;
//    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void processBeforeCommitTransaction(JoinPoint joinPoint){
        String serviceName = joinPoint.getTarget().getClass().getSimpleName();
        log.debug("[CacheSyncAspect]트랜잭션 시작 : " + serviceName);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void processAfterCommitTransaction(JoinPoint joinPoint){
        String serviceName = joinPoint.getTarget().getClass().getSimpleName();
        log.debug("[CacheSyncAspect]캐시 싱크 : " + serviceName);
        cacheManager.syncAll();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void processAfterCompletionTransaction(JoinPoint joinPoint){
        String serviceName = joinPoint.getTarget().getClass().getSimpleName();
        log.debug("[CacheSyncAspect]트랜잭션 종료 :" + serviceName);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void processAfterRollbackTransaction(JoinPoint joinPoint){
        String serviceName = joinPoint.getTarget().getClass().getSimpleName();
        log.debug("[CacheSyncAspect]캐시 롤백 : " + serviceName);
        cacheManager.rollbackAll();
    }
}
