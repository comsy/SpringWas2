package nr.server.core.cacheRedis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class TestCacheConfig {

    /**
     * 테스트 용 캐시는 SimpleCacheManager 를 사용하게 한다.
     */

    @Bean
    @Primary
    public CacheManager redisCacheManager() {
        return simpleCacheManager();
    }

    @Bean
    public CacheManager redisCacheManager7Day() {
        return simpleCacheManager();
    }

    @Bean
    public CacheManager simpleCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(List.of(
                new ConcurrentMapCache("cache")
        ));
        return cacheManager;
    }
}
