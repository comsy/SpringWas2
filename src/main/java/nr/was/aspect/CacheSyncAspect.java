package nr.was.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.component.cache.CacheManager;
import nr.was.data.domain.EntityRoot;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheSyncAspect {

    private final CacheManager<? extends EntityRoot> cacheManager;

    //@After("@annotation(org.springframework.transaction.annotation.Transactional)")
    @AfterReturning("execution(* nr.was.service.*Service.*(..))")
    public void syncCache( JoinPoint joinPoint){
        log.debug("[CacheSyncAspect]syncCache");
        cacheManager.syncAll();
    }

    @AfterThrowing("execution(* nr.was.service.*Service.*(..))")
    public void rollbackCache( JoinPoint joinPoint){
        log.debug("[CacheSyncAspect]rollbackCache");
        cacheManager.rollbackAll();
    }
}
