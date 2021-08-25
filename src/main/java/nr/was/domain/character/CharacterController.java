package nr.was.domain.character;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.domain.character.api.CharacterAddApi;
import nr.was.domain.character.api.CharacterAddExpApi;
import nr.was.domain.character.api.CharacterFindApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CharacterController {

    private final CharacterService characterService;

    @PostMapping("/character/add")
    public CharacterAddApi.Response addCharacter(@RequestBody @Valid CharacterAddApi.Request request){

        return characterService.add(request);
    }

    @PostMapping("/character/findList")
    public CharacterFindApi.Response findCharacterList(@RequestBody @Valid CharacterFindApi.Request request){

        return characterService.findAll(request);
    }

    @PostMapping("/character/addExp")
    public CharacterAddExpApi.Response cacheTest(@RequestBody @Valid CharacterAddExpApi.Request request){

        Long guid = request.getGuid();
        Long id = request.getId();
        int addExp = request.getAddExp();
        log.debug("guid : " + guid);
        log.debug("id : " + id);
        log.debug("addExp : " + addExp);

        return characterService.addCharacterExp(guid, id, addExp);
    }
}
