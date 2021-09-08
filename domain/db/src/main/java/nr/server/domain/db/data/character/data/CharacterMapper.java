package nr.server.domain.db.data.character.data;

import nr.server.core.base.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CharacterMapper extends GenericMapper<CharacterDto, Character> {
    CharacterMapper INSTANCE = Mappers.getMapper(CharacterMapper.class);
}
