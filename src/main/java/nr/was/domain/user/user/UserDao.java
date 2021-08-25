package nr.was.domain.user.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.global.util.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDao {

    // self-autowired - 클래스 내부에서 AOP 관련 method 호출시 self-invocation 문제생김. https://gmoon92.github.io/spring/aop/2019/04/01/spring-aop-mechanism-with-self-invocation.html
    @Resource
    private UserDao self;

    private final UserRepository repository;
    private final CacheManager<User> cacheManager;

    private String cacheKey(Long guid){
        String classKey = getClass().getSimpleName();
        String key = classKey.substring(0, classKey.indexOf("Dao")).toLowerCase();
        return key +"::"+guid;
    }

    //== CQRS : QUERY ==//
    public List<User> getList(Long guid){
        List<User> entityList = cacheManager.getEntityList(cacheKey(guid), User.class);
        if(entityList == null){
            entityList = repository.findByGuid(guid);
            cacheManager.addEntityList(cacheKey(guid), entityList, true);
        }

        return entityList;
    }

    public Optional<User> getEntity(Long guid){
        List<User> entityList = self.getList(guid);

        // 실수 방지를 위해 Optional로  return
        return entityList.stream()
                .filter(dto -> dto.getGuid().equals(guid))
                .findFirst();
    }

    //== CQRS : COMMAND ==//
    public Long saveEntity(User entity){
        repository.save(entity);

        cacheManager.setEntity(cacheKey(entity.getGuid()), entity, User.class);

        return entity.getGuid();  // [CQRS위반] 성능을위해 id는 반환
    }

    public void deleteEntity(User entity){
        repository.delete(entity);

        cacheManager.delEntity(cacheKey(entity.getGuid()), entity);
    }
}
