package nr.was.component.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CacheUtil2<T> {
    private final RedisTemplate<String, T> redisTemplate;

    //region String
    public void putCache(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void putCache(String key, T value, Long expirationTime){
        if(expirationTime != null){
            redisTemplate.opsForValue().set(key, value, expirationTime, TimeUnit.SECONDS);
        }else{
            redisTemplate.opsForValue().set(key, value);
        }
    }

    public T getCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setExpire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }
    //endregion

    public void delCache(String key) {
        redisTemplate.delete(key);
    }
}