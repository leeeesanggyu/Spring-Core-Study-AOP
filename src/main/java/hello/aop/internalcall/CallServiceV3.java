package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV3 {

    private final ObjectProvider<CallServiceV3> callServiceV3ObjectProvider;

    public CallServiceV3(ObjectProvider<CallServiceV3> callServiceV3ObjectProvider) {
        this.callServiceV3ObjectProvider = callServiceV3ObjectProvider;
    }

    public void external() {
        log.info("external call");
        CallServiceV3 callServiceV3 = callServiceV3ObjectProvider.getObject();
        callServiceV3.internal(); // 외부 메소드 호출
    }

    public void internal() {
        log.info("internal call");
    }
}
