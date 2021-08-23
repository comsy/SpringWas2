package nr.was.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class EntityRoot {
    @JsonIgnore
    public abstract String getCacheKey();
}
