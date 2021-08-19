package nr.was.data.dao;

import nr.was.component.CacheUtil;
import nr.was.data.domain.Character;
import nr.was.data.dto.CharacterDto;
import nr.was.data.repository.master.CharacterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CharacterDaoService {

    // self-autowired
    @Resource
    private CharacterDaoService self;

    private final CharacterRepository characterRepository;
    private final CacheUtil<List<CharacterDto>> cacheUtil;

    private String redisKey(Long guid){
        String key = "characterList";
        return key +"::"+guid;
    }

    //== CQRS : QUERY ==//
    public List<Character> getList(Long guid){

        List<CharacterDto> cachedDtoList = cacheUtil.getValue(redisKey(guid));
        if(cachedDtoList == null){
            List<Character> dtoList = characterRepository.findByGuid(guid);
            cachedDtoList = CharacterDto.from(dtoList);

            cacheUtil.putValue(redisKey(guid), cachedDtoList);
        }

        return CharacterDto.toEntityList(cachedDtoList);
    }

    public Optional<Character> getEntity(Long guid, Long id){
        // 같은 클래스 내의 메소드 호출 시 Cacheable 자동 주입.
        List<Character> dtoList = self.getList(guid);

        // 실수 방지를 위해 Optional로  return
        return dtoList.stream()
                .filter(dto -> dto.getId().equals(id))
                .findFirst();
    }

    //== CQRS : COMMAND ==//
    public Long saveEntity(Character entity){
        characterRepository.save(entity);

        return entity.getId();  // [CQRS위반] 성능을위해 id는 반환
    }

    public void deleteEntity(Character entity){
        characterRepository.delete(entity);
    }
}
