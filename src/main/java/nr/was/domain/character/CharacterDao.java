package nr.was.domain.character;

import nr.was.domain.character.repository.CharacterRepository;
import nr.was.global.util.cache.CacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CharacterDao {

    // self-autowired
    @Resource
    private CharacterDao self;

    private final CharacterRepository repository;
    private final CacheManager<Character> cacheManager;

    private String redisKey(Long guid){
        String key = "character";
        return key +"::"+guid;
    }

    //== CQRS : QUERY ==//
    public List<Character> getList(Long guid){
        List<Character> entityList = cacheManager.getEntityList(redisKey(guid), Character.class);
        if(entityList == null){
            entityList = repository.findByGuid(guid);
            cacheManager.addEntityList(redisKey(guid), entityList, true);
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

        cacheManager.setEntity(redisKey(entity.getGuid()), entity, Character.class);

        return entity.getId();  // [CQRS위반] 성능을위해 id는 반환
    }

    public void deleteEntity(Character entity){
        repository.delete(entity);

        cacheManager.delEntity(redisKey(entity.getGuid()), entity);
    }
}
