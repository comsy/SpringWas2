package nr.was.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.component.cache.CacheSyncUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheSyncAspect {

    private final CacheSyncUtil<Object> cacheSyncUtil;

    //@After("@annotation(org.springframework.transaction.annotation.Transactional)")
    @AfterReturning("execution(* nr.was.service.*Service.*(..))")
    public void syncCache( JoinPoint joinPoint){
        log.debug("[CacheSyncAspect]syncCache");
        cacheSyncUtil.syncAll();
    }

    @AfterThrowing("execution(* nr.was.service.*Service.*(..))")
    public void rollbackCache( JoinPoint joinPoint){
        log.debug("[CacheSyncAspect]rollbackCache");
        cacheSyncUtil.rollbackAll();
    }
}
