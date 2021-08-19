package nr.was.configuration;

import lombok.extern.slf4j.Slf4j;
import org.komamitsu.fluency.Fluency;
import org.komamitsu.fluency.fluentd.FluencyBuilderForFluentd;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FluentdConfiguration {

    // DEFAULT
    @Value("${fluentd.defaults.host}")
    private String host;

    @Value("${fluentd.defaults.port}")
    private int port;

    public static String tag;

    // DW
    @Value("${fluentd.dw.host}")
    private String dwHost;

    @Value("${fluentd.dw.port}")
    private int dwPort;

    public static String dwTag;

    // static
    @Value("${fluentd.defaults.tag}")
    private void setTag(String defaultTag){
        tag = defaultTag;
    }

    @Value("${fluentd.dw.tag}")
    private void setDwTag(String tag){
        dwTag = tag;
    }

    @Bean
    public Fluency fluency() {
        // Single Fluentd
        //   - TCP heartbeat (by default)
        //   - Asynchronous flush (by default)
        //   - Without ack response (by default)
        //   - Flush attempt interval is 600ms (by default)
        //   - Initial chunk buffer size is 1MB (by default)
        //   - Threshold chunk buffer size to flush is 4MB (by default)
        //   - Threshold chunk buffer retention time to flush is 1000 ms (by default)
        //   - Max total buffer size is 512MB (by default)
        //   - Use off heap memory for buffer pool (by default)
        //   - Max retries of sending events is 8 (by default)
        //   - Max wait until all buffers are flushed is 10 seconds (by default)
        //   - Max wait until the flusher is terminated is 10 seconds (by default)
        //   - Socket connection timeout is 5000 ms (by default)
        //   - Socket read timeout is 5000 ms (by default)
        //   - 세팅 변경 원하면 https://github.com/komamitsu/fluency  참고
        FluencyBuilderForFluentd builder = new FluencyBuilderForFluentd();
        return builder.build(host, port);
    }

    @Bean
    public Fluency dwFluency() {
        FluencyBuilderForFluentd builder = new FluencyBuilderForFluentd();
        return builder.build(dwHost, dwPort);
    }
}
