package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod = {}", helloMethod);
    }

    @Test
    void exactMatch() throws NoSuchMethodException {
        // pointcut hello method apply
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut.matches(
            MemberServiceImpl.class.getMethod("internal", String.class),
            MemberServiceImpl.class
        )).isFalse();
    }

    @Test
    void allMatch() throws NoSuchMethodException {
        // pointcut all method apply
        pointcut.setExpression("execution(* *(..))");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut.matches(
            MemberServiceImpl.class.getMethod("internal", String.class),
            MemberServiceImpl.class
        )).isTrue();
    }

    @Test
    void nameMatch() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut.matches(
                MemberServiceImpl.class.getMethod("internal", String.class),
                MemberServiceImpl.class
        )).isTrue();
    }

    @Test
    void namePatternMatch() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *el*(..))");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut.matches(
                MemberServiceImpl.class.getMethod("internal", String.class),
                MemberServiceImpl.class
        )).isFalse();
    }

    @Test
    void packageMatch() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *el*(..))");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut.matches(
                MemberServiceImpl.class.getMethod("internal", String.class),
                MemberServiceImpl.class
        )).isFalse();
    }

    @Test
    void packageMatchFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.*.*(..))");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
        Assertions.assertThat(pointcut.matches(
                MemberServiceImpl.class.getMethod("internal", String.class),
                MemberServiceImpl.class
        )).isFalse();
    }

    @Test
    void packageMatchSubPackage() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop..*.*(..))");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut.matches(
                MemberServiceImpl.class.getMethod("internal", String.class),
                MemberServiceImpl.class
        )).isTrue();
    }

    @Test
    void typeMatchSuperType() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberService.class)).isTrue();
        Assertions.assertThat(pointcut.matches(
                MemberServiceImpl.class.getMethod("internal", String.class),
                MemberService.class
        )).isFalse();
    }

    @Test
    void argsMatch() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *(String))");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut.matches(
                MemberServiceImpl.class.getMethod("internal", String.class),
                MemberServiceImpl.class
        )).isTrue();
    }

    @Test
    void noArgsMatch() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *())");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
        Assertions.assertThat(pointcut.matches(
                MemberServiceImpl.class.getMethod("internal", String.class),
                MemberServiceImpl.class
        )).isFalse();
    }

    @Test
    void noArgsMatchStar() throws NoSuchMethodException {
        // exact one parameter, All type allow
        pointcut.setExpression("execution(* *(*))");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut.matches(
                MemberServiceImpl.class.getMethod("internal", String.class),
                MemberServiceImpl.class
        )).isTrue();
    }

    @Test
    void allArgsMatch() throws NoSuchMethodException {
        // All parameter allow
        pointcut.setExpression("execution(* *(..))");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut.matches(
                MemberServiceImpl.class.getMethod("internal", String.class),
                MemberServiceImpl.class
        )).isTrue();
    }

    @Test
    void argsMatchComplex() throws NoSuchMethodException {
        // String type started all parameter, all type allow
        // (String), (String, xxx), (String, xxx, xxx)
        pointcut.setExpression("execution(* *(String, ..))");
//        pointcut.setExpression("execution(* *(String, String))");
//        pointcut.setExpression("execution(* *(String, *))");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut.matches(
                MemberServiceImpl.class.getMethod("internal", String.class),
                MemberServiceImpl.class
        )).isTrue();
    }
}
