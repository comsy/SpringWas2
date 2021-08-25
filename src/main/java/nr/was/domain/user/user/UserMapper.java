package nr.was.domain.user.user;

import nr.was.global.dtomapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper extends GenericMapper<UserDto, User> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
