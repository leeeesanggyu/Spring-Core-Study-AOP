package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;

@Slf4j
@Aspect
public class AspectV6Advice {

    // hello.aop.order 패키지와 하위패키지이면서 클래스 이름 패턴이 Service 인것
    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // @Before
            log.info("[Transaction Start] {}", joinPoint.getSignature());
            final Object result = joinPoint.proceed();
            // @AfterReturning
            log.info("[Transaction End] {}", joinPoint.getSignature());
            return result;
        } catch (IllegalStateException e) {
            // @AfterThrowing
            log.info("[Transaction Rollback] {}", joinPoint.getSignature());
            throw e;
        } finally {
            // @After
            log.info("[Resource Release] {}", joinPoint.getSignature());
        }
    }

    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        log.info("[before] {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return] {} result -> {}", joinPoint.getSignature(), result);
    }

//    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.allOrder()", returning = "result")
//    public void doReturn2(JoinPoint joinPoint, String result) {
//        log.info("[return2] {} result -> {}", joinPoint.getSignature(), result);
//    }

    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[throw] {} message -> {}", joinPoint.getSignature(), ex.getMessage());
    }

    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[info] {}", joinPoint.getSignature());
    }
}

