package nr.was.component.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.data.domain.EntityRoot;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheUtilManager<T extends EntityRoot> {

    private final CacheUtil<T> cacheUtil;
    private final ObjectMapper objectMapper;

    public List<T> getCache(String key, Class<T> parsingClassType) {
        try {
            List<T> cache = cacheUtil.getCache(key);

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
            cacheUtil.putCache(key, value);
        }
        catch (RedisException  | RedisConnectionFailureException e){
            log.error("[CacheManager]putCache error : " + e.getMessage());
        }
    }

    public void delCache(String key) {
        try {
            cacheUtil.delCache(key);
        }
        catch (RedisException | RedisConnectionFailureException e){
            log.error("[CacheManager]delCache error : " + e.getMessage());
        }
    }
}