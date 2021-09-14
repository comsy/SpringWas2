package nr.server.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.server.common.aspect.RequestInfo;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final RequestInfo requestInfo;

    private StopWatch stopWatch;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        stopWatch = new StopWatch();
        stopWatch.start();

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        // POST 일때만 동작
        if(!request.getMethod().equalsIgnoreCase("POST"))
            return;
        // JSON 일때만 동작
        if(!request.getContentType().equalsIgnoreCase("application/json"))
            return;
        if(!response.getContentType().equalsIgnoreCase("application/json"))
            return;

        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;

        stopWatch.stop();
        Long total = stopWatch.getTotalTimeMillis();

        // GSON
        Gson gson = new Gson();
        JsonObject logging2 = new JsonObject();
        JsonObject reqObject = gson.fromJson(objectMapper.readTree(cachingRequest.getContentAsByteArray()).toString(), JsonObject.class);
        JsonObject resObject = gson.fromJson(objectMapper.readTree(cachingResponse.getContentAsByteArray()).toString(), JsonObject.class);
        logging2.add("req", reqObject);
        logging2.add("res", resObject);
        logging2.addProperty("time", total);
        logging2.addProperty("apiExecutionTime", requestInfo.getApiExecutionTime());
        log.info(logging2.toString());

        // JSON simple - 편하긴 한데 put 한 순서대로 출력이 안되서 여기선 쓰기 힘듬.
//        JSONObject logging = new JSONObject();
//        logging.put("req", objectMapper.readTree(cachingRequest.getContentAsByteArray()));
//        logging.put("res", objectMapper.readTree(cachingResponse.getContentAsByteArray()));
//        logging.put("time", total);
//        logging.put("apiExecutionTime", requestInfo.getApiExecutionTime());
//        log.info(logging.toJSONString());

//        log.info(
//                "{\"req\":{},\"res\":{},\"time\":{},\"apiExecutionTime\":{}}",
//                objectMapper.readTree(cachingRequest.getContentAsByteArray()),
//                objectMapper.readTree(cachingResponse.getContentAsByteArray()),
//                total,                              // request 주기 에서 interceptor 사이의 실행시간
//                requestInfo.getApiExecutionTime()   // Controller.api 실행시간
//        );

    }
}