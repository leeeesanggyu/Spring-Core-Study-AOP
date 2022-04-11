package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
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
            log.info("[logArgs2] {}, arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }
        @Before("allMember() && args(args, ..)")
        public void logArgs3(Object args) {
            log.info("[logArgs3] arg = {}", args);
        }
        // 프록시 객체를 전달 받는다.
        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[thisArgs] {}, obj = {}", joinPoint.getSignature(), obj.getClass());
        }
        // 실제 대상 객체를 전달 받는다.
        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[targetArgs] {}, obj = {}", joinPoint.getSignature(), obj.getClass());
        }
        // annotation 을 전달 받는다.
        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@targetArgs] {}, obj = {}", joinPoint.getSignature(), annotation);
        }
        // annotation 을 전달 받는다.
        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@within] {}, obj = {}", joinPoint.getSignature(), annotation);
        }
        // method 의 annotation 을 전달 받는다.
        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[@annotation] {}, annotationValue = {}", joinPoint.getSignature(), annotation.value());
        }
    }
}
