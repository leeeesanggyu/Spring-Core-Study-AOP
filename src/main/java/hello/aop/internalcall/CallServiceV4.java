package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV4 {

    private final InternalServiceV4 internalServiceV4;

    public CallServiceV4(InternalServiceV4 internalServiceV4) {
        this.internalServiceV4 = internalServiceV4;
    }

    public void external() {
        log.info("external call");
        internalServiceV4.internal(); // 외부 메소드 호출
    }

}
