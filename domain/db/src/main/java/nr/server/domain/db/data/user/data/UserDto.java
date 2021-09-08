package nr.server.domain.db.data.user.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import nr.server.domain.db.data.BaseTimeDto;

import java.io.Serializable;
import java.util.List;

@Getter
@SuperBuilder
public class UserDto extends BaseTimeDto implements Serializable {

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