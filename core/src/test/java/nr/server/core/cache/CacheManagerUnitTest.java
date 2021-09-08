package nr.server.core.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nr.server.core.config.CacheConfig;
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
        CacheConfig.class
})
class CacheManagerUnitTest {

    @Autowired
    private CacheUtil<TestEntity> cacheUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, CacheSyncData<TestEntity>> syncDataMap;
    private CacheManager<TestEntity> cacheManager;

    private TestEntity testEntity1;
    private TestEntity testEntity2;
    private TestEntity testEntity3;
    private List<TestEntity> testEntityList;

    private final String cacheKey = "testCache";

    @BeforeEach
    void setUp() {
        this.syncDataMap = new LinkedHashMap<>();
        this.cacheManager = new CacheManager<>(syncDataMap, cacheUtil);

        testEntity1 = TestEntity.builder()
                .id(1L)
                .guid(1L)
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0).build();

        testEntity2 = TestEntity.builder()
                .id(2L)
                .guid(1L)
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0).build();

        testEntity3 = TestEntity.builder()
                .id(2L)
                .guid(1L)
                .characterId(1L)
                .level(2)
                .exp(100)
                .category(0).build();

        testEntityList = new ArrayList<>();
        testEntityList.add(testEntity1);
        testEntityList.add(testEntity2);

        syncDataMap.clear();
    }


    @Test
    @DisplayName("1. 캐시를 저장하고 get 한 데이터는 동일해야한다.")
    void addEntityList() {
        // given

        // when
        cacheManager.addEntityList(cacheKey, testEntityList, true);

        // then
        List<TestEntity> entityList = cacheManager.getEntityList(cacheKey, TestEntity.class);
        assertThat(entityList).hasSameSizeAs(testEntityList);
        assertThat(entityList).containsExactly(testEntity1, testEntity2);
        assertThat(syncDataMap).isNotEmpty();
    }

    @Test
    @DisplayName("2. 캐시를 저장하면 내부 hashmap 에 들어가야하고 dirty:true 가 되어야한다.")
    void addEntityListWithDirty() {
        // given
        assertThat(syncDataMap).isEmpty();

        // when
        cacheManager.addEntityList(cacheKey, testEntityList, true);

        // then
        CacheSyncData<TestEntity> characterCacheSyncData = syncDataMap.get(cacheKey);
        assertThat(syncDataMap).isNotEmpty();
        assertThat(characterCacheSyncData).isNotNull();

        List<TestEntity> dataList = characterCacheSyncData.getDataList();
        assertThat(dataList).isNotEmpty();
        assertThat(dataList).containsExactly(testEntity1, testEntity2);
        assertThat(characterCacheSyncData.getSyncState()).isEqualTo(CacheSyncState.DIRTY);
    }

    @Test
    @DisplayName("3. 캐시를 set 하면 바뀐 데이터가 내부 hashmap 에 들어가야된다")
    void setEntity() {
        // given
        cacheManager.addEntityList(cacheKey, testEntityList, true);

        // when
        cacheManager.setEntity(cacheKey, testEntity3, TestEntity.class);

        // then
        List<TestEntity> entityList = cacheManager.getEntityList(cacheKey, TestEntity.class);
        assertThat(entityList).hasSameSizeAs(testEntityList);
        assertThat(entityList).containsExactly(testEntity1, testEntity3);
        assertThat(entityList).doesNotContain(testEntity2);
    }

    @Test
    @DisplayName("4. 캐시를 del 하면 내부 hashmap 에서 지워져야된다.")
    void delEntity() {
        // given
        cacheManager.addEntityList(cacheKey, testEntityList, true);

        // when
        cacheManager.delEntity(cacheKey, testEntity2);

        // then
        List<TestEntity> entityList = cacheManager.getEntityList(cacheKey, TestEntity.class);
        assertThat(entityList).hasSizeLessThan(testEntityList.size());
        assertThat(entityList).containsExactly(testEntity1);
        assertThat(entityList).doesNotContain(testEntity2);
    }

    @Test
    @DisplayName("5. delCache 은 redis에 저장된 데이터를 지운다.")
    void delCache() {
        // given
        cacheManager.addEntityList(cacheKey, testEntityList, true);
        cacheManager.syncAll();
        syncDataMap.clear();
        assertThat(syncDataMap).isEmpty();
        List<TestEntity> entityList = cacheManager.getEntityList(cacheKey, TestEntity.class);
        assertThat(entityList).isNotNull();

        // when
        cacheManager.delCache(cacheKey);

        // then
        syncDataMap.clear();
        assertThat(syncDataMap).isEmpty();
        entityList = cacheManager.getEntityList(cacheKey, TestEntity.class);
        assertThat(entityList).isNull();
    }

    @Test
    @DisplayName("6. syncAll 은 redis 에 저장되어야한다.")
    void syncAll() {
        // given
        cacheManager.addEntityList(cacheKey, testEntityList, true);

        // when
        cacheManager.syncAll();

        // then
        syncDataMap.clear();
        assertThat(syncDataMap).isEmpty();
        List<TestEntity> entityList = cacheManager.getEntityList(cacheKey, TestEntity.class);
        assertThat(entityList).hasSameSizeAs(testEntityList);

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
        cacheManager.addEntityList(cacheKey, testEntityList, true);

        // when
        cacheManager.rollbackAll();

        // then
        syncDataMap.clear();
        assertThat(syncDataMap).isEmpty();
        List<TestEntity> entityList = cacheManager.getEntityList(cacheKey, TestEntity.class);
        assertThat(entityList).isNull();
    }
}