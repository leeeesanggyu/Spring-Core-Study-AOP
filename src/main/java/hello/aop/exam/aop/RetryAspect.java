package hello.aop.exam.aop;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object doTrace(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
//        log.info("[Retry] {}, retry = {}", joinPoint.getSignature(), retry);
        int maxRetry = retry.value();
        Exception exceptionHolder = null;

        for (int retryCnt=1; retryCnt<=maxRetry; retryCnt++) {
            try {
                log.info("[Retry] {}/{}", retryCnt, maxRetry);
                return joinPoint.proceed();
            } catch (Exception e) {
                log.error(e.getMessage());
                exceptionHolder = e;
            }
        }
        throw exceptionHolder;
    }
}
