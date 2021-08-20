package nr.was.data.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.component.cache.CacheSyncUtil;
import nr.was.data.domain.User;
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
    private final CacheSyncUtil<User> cacheSyncUtil;

    private String redisKey(Long guid){
        String key = "user";
        return key +"::"+guid;
    }

    //== CQRS : QUERY ==//
    public List<User> getList(Long guid){
        List<User> entityList = cacheSyncUtil.getEntityList(redisKey(guid));
        if(entityList == null){
            entityList = repository.findByGuid(guid);
            cacheSyncUtil.addEntityList(redisKey(guid), entityList, true);
        }

        return entityList;
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

        cacheSyncUtil.setEntity(redisKey(entity.getGuid()), entity);

        return entity.getGuid();  // [CQRS위반] 성능을위해 id는 반환
    }

    public void deleteEntity(User entity){
        repository.delete(entity);

        cacheSyncUtil.delEntity(redisKey(entity.getGuid()), entity);
    }
}
