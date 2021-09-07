package nr.server.core.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ICachedEntity {
    @JsonIgnore
    String getCacheKey();
}
