package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

/**
 * within 사용시 주의해야 할 점이 있다.
 * 표현식에 부모 타입을 지정하면 안된다는 점이다.
 * 정확하게 타입이 맞아야 한다.
 * 이 부분에서 execution 과 차이가 난다.
 */
public class WithinTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void withinExact() {
        pointcut.setExpression("within(hello.aop.member.MemberServiceImpl)");
        Assertions.assertThat(
                pointcut.matches(helloMethod, MemberServiceImpl.class)
        ).isTrue();
    }

    @Test
    void withinStar() {
        pointcut.setExpression("within(hello.aop.member.*Service*)");
        Assertions.assertThat(
                pointcut.matches(helloMethod, MemberServiceImpl.class)
        ).isTrue();
    }

    @Test
    void withinSubPackage() {
        pointcut.setExpression("within(hello.aop..*)");
        Assertions.assertThat(
                pointcut.matches(helloMethod, MemberServiceImpl.class)
        ).isTrue();
    }

    //within 은 인터페이스 사용 불가능
    @Test
    void withinSuperTypeFalse() {
        pointcut.setExpression("within(hello.aop.member.MemberService)");
        Assertions.assertThat(
                pointcut.matches(helloMethod, MemberServiceImpl.class)
        ).isFalse();
    }

    //execution 은 인터페이스 사용 가능
    @Test
    void typeMatchSuperType() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");

        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut.matches(
                MemberServiceImpl.class.getMethod("internal", String.class),
                MemberService.class
        )).isFalse();
    }
}
