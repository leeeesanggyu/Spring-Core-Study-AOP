package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Slf4j
public class AspectV5Order {

    @Aspect
    @Order(1)
    public static class LogAspect {
        @Around("hello.aop.order.aop.Pointcuts.allOrder()")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[log] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

    @Aspect
    @Order(2)
    public static class TxAspect {
        // hello.aop.order 패키지와 하위패키지이면서 클래스 이름 패턴이 Service 인것
        @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
        public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
            try {
                log.info("[Transaction Start] {}", joinPoint.getSignature());
                final Object result = joinPoint.proceed();
                log.info("[Transaction End] {}", joinPoint.getSignature());
                return result;
            } catch (IllegalStateException e) {
                log.info("[Transaction Rollback] {}", joinPoint.getSignature());
                throw e;
            } finally {
                log.info("[Resource Release] {}", joinPoint.getSignature());
            }

        }
    }

}
