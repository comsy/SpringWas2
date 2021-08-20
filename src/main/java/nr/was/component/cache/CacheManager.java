package nr.was.component.cache;

import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheManager<T> {

    private final CacheUtil<T> cacheUtil;

    public T getCache(String key) {
        try {
            return cacheUtil.getCache(key);
        }
        catch (RedisException | RedisConnectionFailureException e){
            log.error("[CacheManager]getCache error : " + e.getMessage());
            return null;
        }
    }

    public void putCache(String key, T value) {
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