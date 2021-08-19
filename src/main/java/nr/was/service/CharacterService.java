package nr.was.service;

import nr.was.api.CharacterAddExpApi;
import nr.was.api.CharacterFindApi;
import nr.was.data.dao.CharacterDaoService;
import nr.was.data.dao.UserDaoService;
import nr.was.data.domain.Character;
import nr.was.data.domain.User;
import nr.was.data.dto.CharacterDto;
import nr.was.exception.BusinessException;
import nr.was.exception.ErrorCode;
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

    private final CharacterDaoService characterDaoService;
    private final UserDaoService userDaoService;


    public CharacterFindApi.Response findAll(Long guid){
        List<Character> characterList = characterDaoService.getList(guid);
        Optional<User> user = userDaoService.getEntity(guid);

        List<CharacterDto> characterDtoList = CharacterDto.from(characterList);
        return new CharacterFindApi.Response(200, "", characterDtoList);
    }

    @Transactional  // write 가 있는 경우
    public CharacterAddExpApi.Response addCharacterExp(Long guid, Long id, int addExp){
        Optional<Character> optCharacter = characterDaoService.getEntity(guid, id);
        Character character = optCharacter.orElseThrow(()->new BusinessException(ErrorCode.CHARACTER_NOT_EXIST, "캐릭터가 없어요."));

        // DDD
        boolean isLevelUp = character.addExpAndLevelUp(addExp);

        characterDaoService.saveEntity(character);

        List<Character> characterList = characterDaoService.getList(guid);

        List<CharacterDto> characterDtoList = CharacterDto.from(characterList);
        return new CharacterAddExpApi.Response(200, "", characterDtoList, isLevelUp);
    }
}
