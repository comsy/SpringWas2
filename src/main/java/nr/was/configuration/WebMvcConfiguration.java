package nr.was.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import nr.was.component.RequestInfo;
import nr.was.interceptor.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;
    private final RequestInfo requestInfo;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor(objectMapper, requestInfo));
    }
}
