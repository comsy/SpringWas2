package nr.was.domain.character.charactersub;

import nr.was.global.dtomapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CharacterSubMapper extends GenericMapper<CharacterSubDto, CharacterSub> {
    CharacterSubMapper INSTANCE = Mappers.getMapper(CharacterSubMapper.class);
}
