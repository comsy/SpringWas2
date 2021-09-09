package nr.server.game.domain.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nr.server.core.cacheRedis.cache.CacheManager;
import nr.server.domain.db.data.character.data.Character;
import nr.server.domain.db.data.character.data.CharacterDao;
import nr.server.game.domain.character.api.CharacterAddApi;
import nr.server.game.domain.character.api.CharacterFindApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 통합 테스트 - TODO : Controller 와 Dao 테스트를 하도록 한다.
 * WebEnvironment.MOCK - 가짜 톰캣으로 테스트
 * WebEnvironment.RANDOM_PORT - 실제 톰캣으로 테스트
 * AutoConfigureMockMvc - MockMvc 를 IOC 에 등록
 *
 */
@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class CharacterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CharacterDao characterDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CacheManager<?> cacheManager;

    @Value("${test.api.version}")
    private String version;

    @Value("${test.api.token}")
    private String token;

    private final Long guid = 1L;

    private Character character1;
    private Character character2;
    private List<Character> characterList;

    @PostConstruct
    void init() {
        character1 = Character.builder()
                .id(1L)
                .guid(1L)
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        character2 = Character.builder()
                .id(2L)
                .guid(1L)
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        characterList = new ArrayList<>();
        characterList.add(character1);
        characterList.add(character2);
    }

    @AfterEach
    void tearDown() {
        // 캐시매니저는 Service 단위로만 AOP 진행되므로 강제로 호출필요.
        cacheManager.rollbackAll();
    }

    @Test
    public void DAO_getList() throws Exception {
        //given

        //when
        List<Character> characterList = characterDao.getList(guid);

        //then
        assertThat(characterList)
                .hasSize(0);
    }

    @Test
    public void DAO_getList_saveEntity() throws Exception {
        //given
        List<Character> characterList = characterDao.getList(guid);
        assertThat(characterList)
                .hasSize(0);

        characterDao.saveEntity(character1);
        characterDao.saveEntity(character2);

        //when
        characterList = characterDao.getList(guid);

        //then
        assertThat(characterList)
                .hasSize(2);
    }

    @Test
    public void DAO_getEntity() throws Exception {
        //given
        List<Character> characterList = characterDao.getList(guid);
        characterDao.saveEntity(character1);
        characterDao.saveEntity(character2);
        // repository flush and cache sync
        characterDao.flush();
        characterDao.sync(character1.getGuid());

        //when
        Optional<Character> entity = characterDao.getEntity(guid, character1.getId());

        //then
        Character character = entity.orElse(null);
        assertThat(character).isNotNull();
        assertThat(character).isEqualTo(character1);
    }

    @Test
    public void 컨트롤러_findAll() throws Exception {
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
    public void 컨트롤러_add() throws Exception {
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

        String s = objectMapper.writeValueAsString(date);
        log.debug(s);
    }
}