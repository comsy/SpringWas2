package nr.was.global.util.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nr.was.domain.character.data.Character;
import nr.was.global.configuration.CacheConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = {
        CacheUtil.class,
        ObjectMapper.class,
        CacheConfiguration.class
})
class CacheManagerUnitTest {

    @Autowired
    private CacheUtil<Character> cacheUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, CacheSyncData<Character>> syncDataMap;
    private CacheManager<Character> cacheManager;

    private Character character1;
    private Character character2;
    private Character character3;
    private List<Character> characterList;

    private final String cacheKey = "testCache";

    @BeforeEach
    void setUp() {
        this.syncDataMap = new LinkedHashMap<>();
        this.cacheManager = new CacheManager<>(syncDataMap, cacheUtil);

        character1 = Character.builder()
                .id(1L)
                .guid(1L)
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0).build();

        character2 = Character.builder()
                .id(2L)
                .guid(1L)
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0).build();

        character3 = Character.builder()
                .id(2L)
                .guid(1L)
                .characterId(1L)
                .level(2)
                .exp(100)
                .category(0).build();

        characterList = new ArrayList<>();
        characterList.add(character1);
        characterList.add(character2);

        syncDataMap.clear();
    }


    @Test
    @DisplayName("1. 캐시를 저장하고 get 한 데이터는 동일해야한다.")
    void addEntityList() {
        // given

        // when
        cacheManager.addEntityList(cacheKey, characterList, true);

        // then
        List<Character> entityList = cacheManager.getEntityList(cacheKey, Character.class);
        assertThat(entityList).hasSameSizeAs(characterList);
        assertThat(entityList).containsExactly(character1, character2);
        assertThat(syncDataMap).isNotEmpty();
    }

    @Test
    @DisplayName("2. 캐시를 저장하면 내부 hashmap 에 들어가야하고 dirty:true 가 되어야한다.")
    void addEntityListWithDirty() {
        // given
        assertThat(syncDataMap).isEmpty();

        // when
        cacheManager.addEntityList(cacheKey, characterList, true);

        // then
        CacheSyncData<Character> characterCacheSyncData = syncDataMap.get(cacheKey);
        assertThat(syncDataMap).isNotEmpty();
        assertThat(characterCacheSyncData).isNotNull();

        List<Character> dataList = characterCacheSyncData.getDataList();
        assertThat(dataList).isNotEmpty();
        assertThat(dataList).containsExactly(character1, character2);
        assertThat(characterCacheSyncData.getSyncState()).isEqualTo(CacheSyncState.DIRTY);
    }

    @Test
    @DisplayName("3. 캐시를 set 하면 바뀐 데이터가 내부 hashmap 에 들어가야된다")
    void setEntity() {
        // given
        cacheManager.addEntityList(cacheKey, characterList, true);

        // when
        cacheManager.setEntity(cacheKey, character3, Character.class);

        // then
        List<Character> entityList = cacheManager.getEntityList(cacheKey, Character.class);
        assertThat(entityList).hasSameSizeAs(characterList);
        assertThat(entityList).containsExactly(character1, character3);
        assertThat(entityList).doesNotContain(character2);
    }

    @Test
    @DisplayName("4. 캐시를 del 하면 내부 hashmap 에서 지워져야된다.")
    void delEntity() {
        // given
        cacheManager.addEntityList(cacheKey, characterList, true);

        // when
        cacheManager.delEntity(cacheKey, character2);

        // then
        List<Character> entityList = cacheManager.getEntityList(cacheKey, Character.class);
        assertThat(entityList).hasSizeLessThan(characterList.size());
        assertThat(entityList).containsExactly(character1);
        assertThat(entityList).doesNotContain(character2);
    }

    @Test
    @DisplayName("5. delCache 은 redis에 저장된 데이터를 지운다.")
    void delCache() {
        // given
        cacheManager.addEntityList(cacheKey, characterList, true);
        cacheManager.syncAll();
        syncDataMap.clear();
        assertThat(syncDataMap).isEmpty();
        List<Character> entityList = cacheManager.getEntityList(cacheKey, Character.class);
        assertThat(entityList).isNotNull();

        // when
        cacheManager.delCache(cacheKey);

        // then
        syncDataMap.clear();
        assertThat(syncDataMap).isEmpty();
        entityList = cacheManager.getEntityList(cacheKey, Character.class);
        assertThat(entityList).isNull();
    }

    @Test
    @DisplayName("6. syncAll 은 redis 에 저장되어야한다.")
    void syncAll() {
        // given
        cacheManager.addEntityList(cacheKey, characterList, true);

        // when
        cacheManager.syncAll();

        // then
        syncDataMap.clear();
        assertThat(syncDataMap).isEmpty();
        List<Character> entityList = cacheManager.getEntityList(cacheKey, Character.class);
        assertThat(entityList).hasSameSizeAs(characterList);

        try {
            log.debug("redis cached : " + objectMapper.writeValueAsString(entityList));
        } catch (JsonProcessingException e) {
            log.debug("redis cached : null");
        }

        cacheManager.delCache(cacheKey);
    }

    @Test
    @DisplayName("7. rollbackAll 은 redis에 저장하지 않고 redis 에 있는것도 지운다.")
    void rollbackAll() {
        // given
        cacheManager.addEntityList(cacheKey, characterList, true);

        // when
        cacheManager.rollbackAll();

        // then
        syncDataMap.clear();
        assertThat(syncDataMap).isEmpty();
        List<Character> entityList = cacheManager.getEntityList(cacheKey, Character.class);
        assertThat(entityList).isNull();

        try {
            log.debug("redis cached : " + objectMapper.writeValueAsString(entityList));
        } catch (JsonProcessingException e) {
            log.debug("redis cached : null");
        }
    }
}