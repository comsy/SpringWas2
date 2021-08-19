package nr.was.data.dtomapper;

import nr.was.data.domain.User;
import nr.was.data.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper extends GenericMapper<UserDto, User>{
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
