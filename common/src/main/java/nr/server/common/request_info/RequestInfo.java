package nr.server.common.request_info;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

/**
 * 리퀘스트 안에서만 살아있는 클래스
 * 리퀘스트 내부 공유 object
 */
@Slf4j
@Component
@RequestScope
@Getter
public class RequestInfo {

    private String uuid;
    private String className;
    private String methodName;
    private String taskName;
    private Long apiExecutionTime;

    public void setTaskName(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
        this.taskName = className+"."+methodName;
    }

    public void setApiExecutionTime(Long apiExecutionTime) {
        this.apiExecutionTime = apiExecutionTime;
    }

    /**
     * 실제 생성 시점은 request 처음이 아니고 처음으로 사용된 시점이다.
     */
    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        log.debug("[RequestInfo] request 스코프 빈 created. uuid = " + uuid);
    }

    @PreDestroy
    public void close() {
        log.debug("[RequestInfo] request 스코프 빈 destroyed. uuid = " + uuid);
    }
}
