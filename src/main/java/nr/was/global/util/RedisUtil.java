package nr.was.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil<T> {
    private final RedisTemplate<String, T> redisDataTemplate;

    //region String
    public void putValue(String key, T value) {
        redisDataTemplate.opsForValue().set(key, value);
    }

    public void putValue(String key, T value, Long expirationTime){
        if(expirationTime != null){
            redisDataTemplate.opsForValue().set(key, value, expirationTime, TimeUnit.SECONDS);
        }else{
            redisDataTemplate.opsForValue().set(key, value);
        }
    }

    public T getValue(String key) {
        return redisDataTemplate.opsForValue().get(key);
    }

    public void setExpire(String key, long timeout, TimeUnit unit) {
        redisDataTemplate.expire(key, timeout, unit);
    }
    //endregion


    //region List
    public void addList(String key, T value) {
        redisDataTemplate.opsForList().leftPush(key, value);
    }

    public List<T> getListMembers(String key) {
        return redisDataTemplate.opsForList().range(key, 0, -1);
    }

    public Long getListSize(String key) {
        return redisDataTemplate.opsForList().size(key);
    }
    //endregion


    //region Set
    @SafeVarargs
    public final void addToSet(String key, T... values) {
        redisDataTemplate.opsForSet().add(key, values);
    }

    public Set<T> getSetMembers(String key) {
        return redisDataTemplate.opsForSet().members(key);
    }
    //endregion


    //region Hash
    public void saveHash(String key, int id, T value) {
        redisDataTemplate.opsForHash().put(key, id, value);
    }

    public T findInHash(String key, int id) {
        return (T) redisDataTemplate.opsForHash().get(key, id);
    }

    public void deleteHash(String key, int id) {
        redisDataTemplate.opsForHash().delete(key, id);
    }
    //endregion

    public void delete(String key) {
        redisDataTemplate.delete(key);
    }
}