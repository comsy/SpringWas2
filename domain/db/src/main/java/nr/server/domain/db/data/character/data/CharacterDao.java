package nr.server.domain.db.data.character.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.server.core.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CharacterDao {

    // self-autowired - 클래스 내부에서 AOP 관련 method 호출시 self-invocation 문제생김. https://gmoon92.github.io/spring/aop/2019/04/01/spring-aop-mechanism-with-self-invocation.html
    @Resource
    private CharacterDao self;

    private final CharacterRepository repository;
    private final CacheManager<Character> cacheManager;

    private String cacheKey(Long guid){
        String classKey = getClass().getSimpleName();
        String key = classKey.substring(0, classKey.indexOf("Dao")).toLowerCase();
        return key +"::"+guid;
    }

    //== CQRS : QUERY ==//
    public List<Character> getList(Long guid){
        List<Character> entityList = cacheManager.getEntityList(cacheKey(guid), Character.class);
        if(entityList == null){
            entityList = repository.findByGuid(guid);
            cacheManager.addEntityList(cacheKey(guid), entityList, true);
        }

        return entityList;
    }

    public Optional<Character> getEntity(Long guid, Long id){
        List<Character> entityList = self.getList(guid);

        // 실수 방지를 위해 Optional로  return
        return entityList.stream()
                .filter(dto -> dto.getId().equals(id))
                .findFirst();
    }

    //== CQRS : COMMAND ==//
    public Long saveEntity(Character entity){
        repository.save(entity);

        cacheManager.setEntity(cacheKey(entity.getGuid()), entity, Character.class);

        return entity.getId();  // [CQRS위반] 성능을위해 id는 반환
    }

    /**
     * save 와 동시에 persist context flush - 쌓여있던 query 한 번에 실행.
     * 주의!!!! 한 transaction 안에 서 여러번 쓸때는 성능에 주의할 것
     * 첫번째 쓴이후 부터 해당 table(DB lock 정책에 따라 column 일수도 있음)은 락에 빠지기 때문.
     * 주의!!!!
     */
    public Long saveEntityAndFlush(Character entity){
        repository.saveAndFlush(entity);

        cacheManager.setEntity(cacheKey(entity.getGuid()), entity, Character.class);

        return entity.getId();  // [CQRS위반] 성능을위해 id는 반환
    }

    public void deleteEntity(Character entity){
        repository.delete(entity);

        cacheManager.delEntity(cacheKey(entity.getGuid()), entity);
    }

    public void deleteCache(Long guid){
        cacheManager.delCache(cacheKey(guid));
    }

    /**
     * persist context flush - 쌓여있던 query 한 번에 실행.
     * 주의!!!! 한 transaction 안에 서 여러번 쓸때는 성능에 주의할 것
     * 첫번째 쓴이후 부터 해당 table(DB lock 정책에 따라 row-level lock 일수도 있음)은 락에 빠지기 때문.
     * 주의!!!!
     */
    public void flush(){
        repository.flush();
    }

    public void sync(Long guid){
        cacheManager.sync(cacheKey(guid));
    }
}
