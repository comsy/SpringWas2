package nr.was.global.aspect;

import nr.was.global.util.RequestInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ControllerAspect {

    private final RequestInfo requestInfo;

    /**
     *   GetMapping 설정된 메소드 또는 클래스 설정
     *   GetMapping 노테이션이 설정된 특정 클래스/메소드에만 AspectJ가 적용됨.
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void GetMapping(){ }

    /**
     *   PostMapping 설정된 메소드 또는 클래스 설정
     *   PostMapping 노테이션이 설정된 특정 클래스/메소드에만 AspectJ가 적용됨.
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void PostMapping(){ }

    //@Around("execution(* *..controller.*.*(..))")
    @Around("PostMapping()||GetMapping()")
    public Object apiExecutionTime( ProceedingJoinPoint pjp) throws Throwable {
        // before advice
        StopWatch sw = new StopWatch();
        sw.start();

        Object result = pjp.proceed();

        // after advice
        sw.stop();
        Long total = sw.getTotalTimeMillis();

        requestInfo.setApiExecutionTime(total);

        log.debug("[ApiExecutionTime] " + requestInfo.getTaskName() + " : " + total + "(ms)");

        return result;
    }

    @Before("PostMapping()||GetMapping()")
    public void setTaskName( JoinPoint joinPoint){
        // 어떤 클래스의 메서드인지 출력하는 정보는 pjp 객체에 있다.
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();

        requestInfo.setTaskName(className, methodName);
    }
}
