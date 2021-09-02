package nr.was.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ICachedEntity {
    @JsonIgnore
    String getCacheKey();
}
