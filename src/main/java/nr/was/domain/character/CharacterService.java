package nr.was.domain.character;

import nr.was.domain.character.api.CharacterAddExpApi;
import nr.was.domain.character.api.CharacterFindApi;
import nr.was.global.exception.BusinessException;
import nr.was.global.exception.ErrorCode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterService {

    private final CharacterDao characterDao;


    public CharacterFindApi.Response findAll(Long guid){
        List<Character> characterList = characterDao.getList(guid);

        List<CharacterDto> characterDtoList = CharacterDto.from(characterList);
        return new CharacterFindApi.Response(characterDtoList);
    }

    @Transactional  // write 가 있는 경우
    public CharacterAddExpApi.Response addCharacterExp(Long guid, Long id, int addExp){
        Optional<Character> optCharacter = characterDao.getEntity(guid, id);
        Character character = optCharacter.orElseThrow(()->new BusinessException(ErrorCode.CHARACTER_NOT_EXIST, "캐릭터가 없어요."));

        // DDD
        boolean isLevelUp = character.addExpAndLevelUp(addExp);

        characterDao.saveEntity(character);

        List<Character> characterList = characterDao.getList(guid);

        List<CharacterDto> characterDtoList = CharacterDto.from(characterList);
        return new CharacterAddExpApi.Response(characterDtoList, isLevelUp);
    }
}