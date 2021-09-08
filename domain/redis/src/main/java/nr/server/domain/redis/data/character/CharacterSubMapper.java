package nr.server.domain.redis.data.character;

import nr.server.core.base.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CharacterSubMapper extends GenericMapper<CharacterSubDto, CharacterSub> {
    CharacterSubMapper INSTANCE = Mappers.getMapper(CharacterSubMapper.class);
}
