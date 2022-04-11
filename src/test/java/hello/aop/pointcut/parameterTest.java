package hello.aop.pointcut;

import hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(parameterTest.ParameterAspect.class)
@SpringBootTest
public class parameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy = {}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {
        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {
        }
        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg = joinPoint.getArgs()[0];
            log.info("[logArgs1] {}, arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }
        @Around("allMember() &&(args(arg, ..))")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs1] {}, arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }
        @Before("allMember() && args(args, ..)")
        public void logArgs3(Object args) {
            log.info("[logArgs3] arg = {}", args);
        }
    }
}
