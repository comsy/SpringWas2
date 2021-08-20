package nr.was.data.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.component.cache.CacheSyncUtil;
import nr.was.data.domain.User;
import nr.was.data.dto.UserDto;
import nr.was.data.repository.master.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDao {

    // self-autowired
    @Resource
    private UserDao self;

    private final UserRepository repository;
    private final CacheSyncUtil<UserDto> cacheSyncUtil;

    private String redisKey(Long guid){
        String key = "user";
        return key +"::"+guid;
    }

    //== CQRS : QUERY ==//
    public List<User> getList(Long guid){
        List<UserDto> cachedDtoList = cacheSyncUtil.getDataList(redisKey(guid));
        if(cachedDtoList == null){
            List<User> dtoList = repository.findByGuid(guid);
            cachedDtoList = UserDto.from(dtoList);
            cacheSyncUtil.addDataList(redisKey(guid), cachedDtoList, true);
        }

        return UserDto.toEntityList(cachedDtoList);
    }

    public Optional<User> getEntity(Long guid){
        List<User> dtoList = self.getList(guid);

        // 실수 방지를 위해 Optional로  return
        return dtoList.stream()
                .filter(dto -> dto.getGuid().equals(guid))
                .findFirst();
    }

    //== CQRS : COMMAND ==//
    public Long saveEntity(User entity){
        repository.save(entity);

        UserDto dto = UserDto.from(entity);
        cacheSyncUtil.setData(redisKey(dto.getGuid()), dto);

        return entity.getGuid();  // [CQRS위반] 성능을위해 id는 반환
    }

    public void deleteEntity(User entity){
        repository.delete(entity);

        UserDto dto = UserDto.from(entity);
        cacheSyncUtil.delDate(redisKey(dto.getGuid()), dto);
    }
}
