package nr.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 통합 테스트
 * WebEnvironment.MOCK - 가짜 톰캣으로 테스트
 * WebEnvironment.RANDOM_PORT - 실제 톰캣으로 테스트
 * AutoConfigureMockMvc - MockMvc 를 IOC 에 등록
 *
 */

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
public class GameApplicationTest {

}