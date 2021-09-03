package nr.was.domain.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nr.was.domain.character.api.CharacterAddApi;
import nr.was.domain.character.api.CharacterFindApi;
import nr.was.domain.character.data.CharacterDao;
import nr.was.domain.character.data.Character;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
@AutoConfigureCache(cacheProvider = CacheType.SIMPLE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class CharacterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CharacterDao characterDao;

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

    @Test
    public void Auditing테스트() throws Exception {
        //given
        List<Character> list = characterDao.getList(guid);
        assertThat(list).isEmpty();
        Character character = Character.builder()
                .guid(guid)
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0).build();
        characterDao.saveEntity(character);

        list = characterDao.getList(guid);
        assertThat(list).isNotEmpty();

        Character character1 = list.get(0);
        LocalDateTime modifiedDate = character1.getModifiedDate();
        log.debug("modifiedDate : " + modifiedDate.toString());

        //when
        character1.changeLevel();
        String modified1 = character1.getModifiedDate().toString();
        characterDao.saveEntity(character1);
        String modified2 = character1.getModifiedDate().toString();

        characterDao.flush();
        String modified3 = character1.getModifiedDate().toString();
        characterDao.sync(guid);

        //then
        assertThat(modified1).isEqualTo(modified2);
        assertThat(modified1).isNotEqualTo(modified3);
        list = characterDao.getList(guid);
        assertThat(list).isNotEmpty();

        Character character2 = list.get(0);
        assertThat(modified1).isNotEqualTo(character2.getModifiedDate().toString());

        characterDao.deleteCache(guid);

    }

    @Test
    public void datetime() throws Exception {
        //given
        LocalDateTime date = LocalDateTime.now();
        log.debug(date.toString());

        //when
        String format = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
        LocalDateTime date2 = LocalDateTime.parse(format, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
        log.debug(format);
        //then
        assertThat(date.toString()).isEqualTo(date2.toString());
    }
}