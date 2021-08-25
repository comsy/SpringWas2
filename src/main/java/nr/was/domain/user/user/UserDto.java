package nr.was.domain.user.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class UserDto implements Serializable {

    @JsonProperty("guid")
    private Long guid;

    @JsonProperty("level")
    private int level;

    @JsonProperty("exp")
    private int exp;

    public static UserDto from(User entity){
        return UserMapper.INSTANCE.toDto(entity);
    }

    public static List<UserDto> from(List<User> entityList){
        return UserMapper.INSTANCE.toDtoList(entityList);
    }

    public User toEntity(){
        return UserMapper.INSTANCE.toEntity(this);
    }

    public static List<User> toEntityList(List<UserDto> dtoList){
        return UserMapper.INSTANCE.toEntityList(dtoList);
    }
}