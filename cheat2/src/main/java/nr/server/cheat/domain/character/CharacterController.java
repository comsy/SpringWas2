package nr.server.cheat.domain.character;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.server.cheat.domain.dto.CharacterAddRequest;
import nr.server.cheat.domain.dto.CharacterAddResponse;
import nr.server.cheat.domain.dto.UserDTO;
import nr.server.domain.db.data.character.data.CharacterDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @RequestMapping(value = "/character", method = RequestMethod.GET)
    public String characterForm(Model model) {
        model.addAttribute("characterAddInfo", new CharacterAddRequest());
        model.addAttribute("userInfo", new UserDTO());
        return "character";
    }

    @RequestMapping(value = "/character/add", method = RequestMethod.POST)
    public String send(@ModelAttribute("characterAddInfo") CharacterAddRequest characterAddInfo, Model model)
    {
        log.info("guid " + characterAddInfo.getGuid());
        log.info("cid " + characterAddInfo.getCharacterId());

        List<CharacterDto> result = characterService.add(characterAddInfo.getGuid(), characterAddInfo.getCharacterId());

        log.info("response size " + result.size());
        model.addAttribute("characterDtoList", result);
        model.addAttribute("userInfo", new UserDTO());
        return "character";
    }
}
