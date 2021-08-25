package nr.was.domain.character.character;

import nr.was.domain.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CharacterMapper extends GenericMapper<CharacterDto, Character> {
    CharacterMapper INSTANCE = Mappers.getMapper(CharacterMapper.class);
}