package nr.was.domain.character.dataredis;

import nr.was.domain.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CharacterSubMapper extends GenericMapper<CharacterSubDto, CharacterSub> {
    CharacterSubMapper INSTANCE = Mappers.getMapper(CharacterSubMapper.class);
}
