package nr.was.component.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CacheUtil<T> {

    @Cacheable(value = "cache", key = "#key", cacheManager = "redisCacheManager", unless = "#result == null")
    public T getCache(String key) {
        return null;
    }

    @CachePut(value = "cache", key = "#key", cacheManager = "redisCacheManager", unless = "#result == null")
    public T putCache(String key, T value) {
        return value;
    }

    @CacheEvict(value = "cache", key = "#key", cacheManager = "redisCacheManager")
    public void delCache(String key) {
    }
}