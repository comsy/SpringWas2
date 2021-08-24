package nr.was.global.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.global.util.cache.CacheManager;
import nr.was.global.entity.EntityMaster;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheSyncAspect {

    private final CacheManager<? extends EntityMaster> cacheManager;

    //@After("@annotation(org.springframework.transaction.annotation.Transactional)")
    @AfterReturning("execution(* *..*.*Service.*(..))")

    public void syncCache( JoinPoint joinPoint){
        log.debug("[CacheSyncAspect]syncCache");
        cacheManager.syncAll();
    }

    @AfterThrowing("execution(* *..*.*Service.*(..))")
    public void rollbackCache( JoinPoint joinPoint){
        log.debug("[CacheSyncAspect]rollbackCache");
        cacheManager.rollbackAll();
    }
}
