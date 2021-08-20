package nr.was.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import nr.was.data.domain.Character;
import nr.was.data.dtomapper.CharacterMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class CharacterDto implements Serializable {
    @JsonProperty("dbKey")
    private Long id;

    @JsonProperty("guid")
    private Long guid;

    @JsonProperty("category")
    private int category;

    @JsonProperty("id")
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