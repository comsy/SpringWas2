package nr.was.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface CachedEntityInterface {
    @JsonIgnore
    String getCacheKey();
}
