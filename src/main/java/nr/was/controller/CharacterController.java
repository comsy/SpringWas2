package nr.was.controller;

import nr.was.api.CharacterAddExpApi;
import nr.was.api.CharacterFindApi;
import nr.was.service.CharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CharacterController {

    private final CharacterService characterService;

    @PostMapping("/character/findList")
    public CharacterFindApi.Response findCharacterList(@RequestBody @Valid CharacterFindApi.Request request){

        Long guid = request.getGuid();
        log.debug("guid : " + guid);

        return characterService.findAll(guid);
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
