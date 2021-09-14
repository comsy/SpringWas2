package nr.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class CheatApplication {
    public static void main(String[] args) {
        SpringApplication.run(CheatApplication.class, args);
    }
}
