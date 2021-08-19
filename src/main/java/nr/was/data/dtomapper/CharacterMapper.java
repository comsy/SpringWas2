package nr.was.data.dtomapper;

import nr.was.data.domain.Character;
import nr.was.data.dto.CharacterDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CharacterMapper extends GenericMapper<CharacterDto, Character>{
    CharacterMapper INSTANCE = Mappers.getMapper(CharacterMapper.class);
}
