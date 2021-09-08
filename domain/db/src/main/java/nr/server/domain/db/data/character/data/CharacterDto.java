package nr.server.domain.db.data.character.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import nr.server.domain.db.data.BaseTimeDto;

import java.io.Serializable;
import java.util.List;

@Getter
@SuperBuilder
public class CharacterDto extends BaseTimeDto implements Serializable {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("guid")
    private Long guid;

    @JsonProperty("category")
    private int category;

    @JsonProperty("character_id")
    private Long characterId;

    @JsonProperty("level")
    private int level;

    @JsonProperty("exp")
    private int exp;

    public static CharacterDto from(Character entity){
        return CharacterMapper.INSTANCE.toDto(entity);
    }

    public static List<CharacterDto> from(List<Character> entityList){
        return CharacterMapper.INSTANCE.toDtoList(entityList);
    }

    public Character toEntity(){
        return CharacterMapper.INSTANCE.toEntity(this);
    }

    public static List<Character> toEntityList(List<CharacterDto> dtoList){
        return CharacterMapper.INSTANCE.toEntityList(dtoList);
    }
}