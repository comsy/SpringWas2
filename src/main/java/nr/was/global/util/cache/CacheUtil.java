package nr.was.global.util.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.global.entity.EntityMaster;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheUtil<T extends EntityMaster> {

    private final ObjectMapper objectMapper;

    public List<T> getCache(String key, Class<T> parsingClassType) {
        try {
            List<T> cache = this.get(key);

            if(cache == null)
                log.debug("[CacheUtil] cache is null : " + key);
            else
                log.debug("[CacheUtil] cache is not null : " + key);

            // LinkedHashMap to Entity
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, parsingClassType);
            cache = objectMapper.convertValue(cache, collectionType);

            return cache;
        }
        catch (RedisException | RedisConnectionFailureException e){
            log.error("[CacheManager]getCache error : " + e.getMessage());
            return null;
        }
    }

    public void putCache(String key, List<T> value) {
        try{

            List<T> put = this.put(key, value);
            put.forEach(data->log.debug("[putCache]" + data.toString()));
        }
        catch (RedisException  | RedisConnectionFailureException e){
            log.error("[CacheManager]putCache error : " + e.getMessage());
        }
    }

    public void delCache(String key) {
        try {
            this.del(key);
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