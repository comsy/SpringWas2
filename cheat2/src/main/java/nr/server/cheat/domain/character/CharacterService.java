package nr.server.cheat.domain.character;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.server.domain.db.data.character.data.Character;
import nr.server.domain.db.data.character.data.CharacterDao;
import nr.server.domain.db.data.character.data.CharacterDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterService {


    private final CharacterDao characterDao;

    List<CharacterDto> add(Long guid, Long characterId) {
        Character character = Character.builder()
                .guid(guid)
                .characterId(characterId)
                .category(0)
                .level(1)
                .exp(0)
                .build();

        characterDao.saveEntity(character);

        List<Character> characterList = characterDao.getList(guid);

        List<CharacterDto> characterDtoList = CharacterDto.from(characterList);

        return characterDtoList;
    }
}
