package nr.was.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface DtoRoot {
    @JsonIgnore
    String getCacheKey();
}
