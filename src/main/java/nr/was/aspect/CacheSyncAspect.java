package nr.was.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.component.CacheSyncUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheSyncAspect {

    private final CacheSyncUtil cacheSyncUtil;

    //@After("@annotation(org.springframework.transaction.annotation.Transactional)")
    @After("execution(* nr.was.service.*Service.*(..))")
    public void syncCache( JoinPoint joinPoint){
        log.debug("[CacheSyncAspect]syncCache");
        cacheSyncUtil.syncAll();
    }
}
