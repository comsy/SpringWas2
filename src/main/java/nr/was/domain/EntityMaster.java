package nr.was.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class EntityMaster {
    @JsonIgnore
    public abstract String getCacheKey();
}
