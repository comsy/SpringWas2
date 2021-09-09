package nr.server.domain.db.data.user.data;

import nr.server.core.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper extends GenericMapper<UserDto, User> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
