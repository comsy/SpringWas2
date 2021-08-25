package nr.was.domain.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nr.was.domain.character.api.CharacterAddApi;
import nr.was.domain.character.api.CharacterFindApi;
import nr.was.domain.character.character.Character;
import nr.was.domain.character.character.CharacterDto;
import nr.was.global.util.RequestInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@WebMvcTest
class CharacterControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CharacterService characterService;

    @MockBean
    private RequestInfo requestInfo;

    @Value("${test.api.version}")
    private String version;

    @Value("${test.api.token}")
    private String token;

    private final Long guid = 1L;

    @Test
    public void 테스트_findAll() throws Exception {
        log.info("테스트_findAll 시작 ===============================================================================");
        //given
        CharacterFindApi.Request request = new CharacterFindApi.Request(version, token, guid);
        String content = new ObjectMapper().writeValueAsString(request);
        log.info("request : " + content);

        Character character = Character.builder()
                .id(1L)
                .guid(request.getGuid())
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0)
                .build();
        CharacterDto characterDto = CharacterDto.from(character);
        List<CharacterDto> characterList = new ArrayList<>();
        characterList.add(characterDto);
        CharacterFindApi.Response response = new CharacterFindApi.Response(characterList);
        String responseJson = new ObjectMapper().writeValueAsString(response);
        log.info("responseJson : " + responseJson);

        when(characterService.findAll(any(CharacterFindApi.Request.class))).thenReturn(response);

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
    public void 테스트_add() throws Exception {
        log.info("테스트_add 시작 ===============================================================================");
        //given
        CharacterAddApi.Request request = new CharacterAddApi.Request(version, token, guid);
        String content = new ObjectMapper().writeValueAsString(request);
        log.info("request : " + content);

        Character character = Character.builder()
                .id(1L)
                .guid(request.getGuid())
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0)
                .build();
        CharacterDto characterDto = CharacterDto.from(character);
        List<CharacterDto> characterList = new ArrayList<>();
        characterList.add(characterDto);
        CharacterAddApi.Response response = new CharacterAddApi.Response(characterList);
        String responseJson = new ObjectMapper().writeValueAsString(response);
        log.info("responseJson : " + responseJson);

        when(characterService.add(any(CharacterAddApi.Request.class))).thenReturn(response);

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