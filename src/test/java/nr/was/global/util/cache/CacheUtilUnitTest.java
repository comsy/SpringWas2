package nr.was.global.util.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nr.was.domain.character.data.Character;
import nr.was.global.configuration.CacheConfiguration;
import nr.was.global.configuration.RedisCacheConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = {
        CacheUtil.class,
        ObjectMapper.class,
        CacheConfiguration.class,
        RedisCacheConfiguration.class
})
@CacheConfig(cacheManager = "simpleCacheManager")
class CacheUtilUnitTest {

    @Autowired
    CacheUtil<Character> cacheUtil;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    private Character character1;
    private Character character2;
    private List<Character> characterList;

    private final String cacheKey = "testCache";

    @BeforeEach
    void setUp() {
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

        characterList = new ArrayList<>();
        characterList.add(character1);
        characterList.add(character2);
    }

    @AfterEach
    void tearDown() {
        cacheUtil.del(cacheKey);
    }

    @Test
    void getCacheNull() {
        // given


        // when
        List<Character> cache = cacheUtil.getCache(cacheKey, Character.class);
        log.info("getCache : " + cache);

        // then
        assertThat(cache).isNull();
    }

    @Test
    void getCacheNotNull() {
        // given
        cacheUtil.put(cacheKey, characterList);

        // when
        List<Character> cache = cacheUtil.getCache(cacheKey, Character.class);
        log.info("getCache : " + cache);

        // then
        assertThat(cache).isNotNull();
        assertThat(cache.get(0).getId()).isEqualTo(character1.getId());
        assertThat(cache.get(0).getGuid()).isEqualTo(character1.getGuid());
        assertThat(cache.get(0).getCharacterId()).isEqualTo(character1.getCharacterId());
        assertThat(cache.get(0).getLevel()).isEqualTo(character1.getLevel());
        assertThat(cache.get(0).getCategory()).isEqualTo(character1.getCategory());
        assertThat(cache.get(1).getId()).isEqualTo(character2.getId());
        assertThat(cache.get(1).getGuid()).isEqualTo(character2.getGuid());
        assertThat(cache.get(1).getCharacterId()).isEqualTo(character2.getCharacterId());
        assertThat(cache.get(1).getLevel()).isEqualTo(character2.getLevel());
        assertThat(cache.get(1).getCategory()).isEqualTo(character2.getCategory());
    }

    @Test
    void putCache() {
        // given

        // when
        cacheUtil.putCache(cacheKey, characterList);

        // then
        List<Character> cache = cacheUtil.getCache(cacheKey, Character.class);
        log.info("putCache : " + cache);

        assertThat(cache).isNotNull();
        assertThat(cache.get(0).getId()).isEqualTo(character1.getId());
        assertThat(cache.get(0).getGuid()).isEqualTo(character1.getGuid());
        assertThat(cache.get(0).getCharacterId()).isEqualTo(character1.getCharacterId());
        assertThat(cache.get(0).getLevel()).isEqualTo(character1.getLevel());
        assertThat(cache.get(0).getCategory()).isEqualTo(character1.getCategory());
        assertThat(cache.get(1).getId()).isEqualTo(character2.getId());
        assertThat(cache.get(1).getGuid()).isEqualTo(character2.getGuid());
        assertThat(cache.get(1).getCharacterId()).isEqualTo(character2.getCharacterId());
        assertThat(cache.get(1).getLevel()).isEqualTo(character2.getLevel());
        assertThat(cache.get(1).getCategory()).isEqualTo(character2.getCategory());

    }

    @Test
    void delCache() {
        // given
        cacheUtil.putCache(cacheKey, characterList);

        // when
        cacheUtil.del(cacheKey);

        // then
        List<Character> cache = cacheUtil.getCache(cacheKey, Character.class);
        log.info("delCache : " + cache);

        assertThat(cache).isNull();
    }
}