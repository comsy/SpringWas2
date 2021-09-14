package nr.server.game.domain.character;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.server.common.exception.BusinessException;
import nr.server.common.exception.ErrorCode;
import nr.server.domain.db.data.character.data.Character;
import nr.server.domain.db.data.character.data.CharacterDao;
import nr.server.domain.db.data.character.data.CharacterDto;
import nr.server.game.domain.character.api.CharacterAddApi;
import nr.server.game.domain.character.api.CharacterAddExpApi;
import nr.server.game.domain.character.api.CharacterFindApi;
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


    public CharacterFindApi.Response findAll(CharacterFindApi.Request request){
        Long guid = request.getGuid();
        List<Character> characterList = characterDao.getList(guid);

        List<CharacterDto> characterDtoList = CharacterDto.from(characterList);
        return new CharacterFindApi.Response(characterDtoList);
    }

    @Transactional  // write 가 있는 경우
    CharacterAddExpApi.Response addCharacterExp(Long guid, Long id, int addExp){
        Optional<Character> optCharacter = characterDao.getEntity(guid, id);
        Character character = optCharacter.orElseThrow(()->new BusinessException(ErrorCode.CHARACTER_NOT_EXIST, "캐릭터가 없어요."));

        // DDD
        boolean isLevelUp = character.addExpAndLevelUp(addExp);

        characterDao.saveEntity(character);

        List<Character> characterList = characterDao.getList(guid);

        List<CharacterDto> characterDtoList = CharacterDto.from(characterList);
        return new CharacterAddExpApi.Response(characterDtoList, isLevelUp);
    }

    @Transactional
    CharacterAddApi.Response add(CharacterAddApi.Request request) {
        Long guid = request.getGuid();
        Character character = Character.builder()
                .guid(guid)
                .characterId(0L)
                .category(0)
                .level(1)
                .exp(0)
                .build();

        characterDao.saveEntity(character);

        List<Character> characterList = characterDao.getList(guid);

        List<CharacterDto> characterDtoList = CharacterDto.from(characterList);

        return new CharacterAddApi.Response(characterDtoList);
    }
}
