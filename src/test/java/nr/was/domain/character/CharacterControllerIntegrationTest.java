package nr.was.domain.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nr.was.domain.character.api.CharacterAddApi;
import nr.was.domain.character.api.CharacterFindApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class CharacterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void 테스트_findAll() throws Exception {
        log.info("테스트_findAll 시작 ===============================================================================");
        //given
        CharacterFindApi.Request request = new CharacterFindApi.Request("1.0.0", "asdf", 100001041L);
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
                .andExpect(jsonPath("$.characterList[0].guid").value(100001041L))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void 테스트_add() throws Exception {
        log.info("테스트_add 시작 ===============================================================================");
        //given
        CharacterAddApi.Request request = new CharacterAddApi.Request("1.0.0", "asdf", 1L);
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