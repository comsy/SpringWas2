package nr.was;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nr.was.domain.character.api.CharacterAddApi;
import nr.was.domain.character.api.CharacterFindApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 통합 테스트
 * WebEnvironment.MOCK - 가짜 톰캣으로 테스트
 * WebEnvironment.RANDOM_PORT - 실제 톰캣으로 테스트
 * AutoConfigureMockMvc - MockMvc 를 IOC 에 등록
 *
 */

@Slf4j
@AutoConfigureMockMvc
@AutoConfigureCache(cacheProvider = CacheType.SIMPLE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class WasApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Value("${test.api.version}")
    private String version;

    @Value("${test.api.token}")
    private String token;

    private final Long guid = 1L;

    @BeforeEach
    void setUp() {

    }

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("Contoller:/character/findList")
    public void characterControllerFindList() throws Exception {
        log.info("테스트_findAll 시작 ===============================================================================");
        //given
        CharacterFindApi.Request request = new CharacterFindApi.Request(version, token, guid);
        String content = new ObjectMapper().writeValueAsString(request);
        log.info("request : " + content);

        //when
        ResultActions resultActions = mockMvc.perform(post("/character/findList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
        );

        log.info("response : " + resultActions.andReturn().getResponse().getContentAsString());

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("Contoller:/character/add")
    public void characterControllerAdd() throws Exception {
        log.info("테스트_add 시작 ===============================================================================");
        //given
        CharacterAddApi.Request request = new CharacterAddApi.Request(version, token, guid);
        String content = new ObjectMapper().writeValueAsString(request);
        log.info("request : " + content);

        //when
        ResultActions resultActions = mockMvc.perform(post("/character/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
        );

        log.info("response : " + resultActions.andReturn().getResponse().getContentAsString());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.characterList[0].guid").value(1L))
                .andDo(MockMvcResultHandlers.print());

    }

}
