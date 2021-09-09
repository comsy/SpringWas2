package nr.server.core.cacheRedis.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ICachedEntity {
    @JsonIgnore
    String getCacheKey();
}
