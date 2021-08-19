package nr.was.component;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CacheUtil<T> {
    private final RedisTemplate<String, T> redisTemplate;

    //region String
    public void putValue(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void putValue(String key, T value, Long expirationTime){
        if(expirationTime != null){
            redisTemplate.opsForValue().set(key, value, expirationTime, TimeUnit.SECONDS);
        }else{
            redisTemplate.opsForValue().set(key, value);
        }
    }

    public T getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setExpire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }
    //endregion


    //region List
    public void addList(String key, T value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public List<T> getListMembers(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }
    //endregion


    //region Set
    @SafeVarargs
    public final void addToSet(String key, T... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    public Set<T> getSetMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }
    //endregion


    //region Hash
    public void saveHash(String key, int id, T value) {
        redisTemplate.opsForHash().put(key, id, value);
    }

    public T findInHash(String key, int id) {
        return (T) redisTemplate.opsForHash().get(key, id);
    }

    public void deleteHash(String key, int id) {
        redisTemplate.opsForHash().delete(key, id);
    }
    //endregion
}