package nr.was.data.dao;

import nr.was.component.cache.CacheSyncUtil;
import nr.was.data.domain.Character;
import nr.was.data.repository.master.CharacterRepository;
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
    private final CacheSyncUtil<Character> cacheSyncUtil;

    private String redisKey(Long guid){
        String key = "character";
        return key +"::"+guid;
    }

    //== CQRS : QUERY ==//
    public List<Character> getList(Long guid){
        List<Character> entityList = cacheSyncUtil.getEntityList(redisKey(guid));
        if(entityList == null){
            entityList = repository.findByGuid(guid);
            cacheSyncUtil.addEntityList(redisKey(guid), entityList, true);
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

        cacheSyncUtil.setEntity(redisKey(entity.getGuid()), entity);

        return entity.getId();  // [CQRS위반] 성능을위해 id는 반환
    }

    public void deleteEntity(Character entity){
        repository.delete(entity);

        cacheSyncUtil.delEntity(redisKey(entity.getGuid()), entity);
    }
}
