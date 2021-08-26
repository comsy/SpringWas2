package nr.was.global.util.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.domain.EntityMaster;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheUtil<T extends EntityMaster> {

    private final ObjectMapper objectMapper;

    // self-autowired - 클래스 내부에서 AOP 관련 method 호출시 self-invocation 문제생김. https://gmoon92.github.io/spring/aop/2019/04/01/spring-aop-mechanism-with-self-invocation.html
    @Resource
    private CacheUtil<T> self;

    public List<T> getCache(String key, Class<T> parsingClassType) {
        try {
            List<T> cacheHashMap = self.get(key);

            // LinkedHashMap to Entity
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, parsingClassType);

            return objectMapper.convertValue(cacheHashMap, collectionType);
        }
        catch (RedisException | RedisConnectionFailureException e){
            log.error("[CacheManager]getCache error : " + e.getMessage());
            return null;
        }
    }

    public void putCache(String key, List<T> value) {
        try{
            self.put(key, value);
        }
        catch (RedisException  | RedisConnectionFailureException e){
            log.error("[CacheManager]putCache error : " + e.getMessage());
        }
    }

    public void delCache(String key) {
        try {
            self.del(key);
        }
        catch (RedisException | RedisConnectionFailureException e){
            log.error("[CacheManager]delCache error : " + e.getMessage());
        }
    }



    @Cacheable(value = "cache", key = "#key", cacheManager = "redisCacheManager", unless = "#result == null")
    public List<T> get(String key) {
        return null;
    }

    @CachePut(value = "cache", key = "#key", cacheManager = "redisCacheManager", unless = "#result == null")
    public List<T> put(String key, List<T> value) {
        return value;
    }

    @CacheEvict(value = "cache", key = "#key", cacheManager = "redisCacheManager")
    public void del(String key) {
    }
}