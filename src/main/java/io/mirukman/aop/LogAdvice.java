package io.mirukman.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class LogAdvice {
    
    @Before( "execution(public * io.mirukman.service..*.*(..))")
    public void logBefore() {
        log.info("I am log advice");
    }

    @Around("execution(public * io.mirukman.service..*.*(..))")
    public Object logTime(ProceedingJoinPoint pjp) {

        long start = System.currentTimeMillis();

        Object targetReturn = null;

        try {
			targetReturn = pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        long end = System.currentTimeMillis();

        log.info("elapsed time: " + (end - start) + "ms");

        return targetReturn;
    }
}
