package nr.was.domain.character.charactersub;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class CharacterSubDto implements Serializable {
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

    public static CharacterSubDto from(CharacterSub entity){
        return CharacterSubMapper.INSTANCE.toDto(entity);
    }

    public static List<CharacterSubDto> from(List<CharacterSub> entityList){
        return CharacterSubMapper.INSTANCE.toDtoList(entityList);
    }

    public CharacterSub toEntity(){
        return CharacterSubMapper.INSTANCE.toEntity(this);
    }

    public static List<CharacterSub> toEntityList(List<CharacterSubDto> dtoList){
        return CharacterSubMapper.INSTANCE.toEntityList(dtoList);
    }
}