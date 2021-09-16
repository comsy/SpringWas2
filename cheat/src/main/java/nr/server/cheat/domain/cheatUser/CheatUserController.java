package nr.server.cheat.domain.cheatUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CheatUserController {
    private final CheatUserService cheatUserService;

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("form", new CheatUserFormDto());

        return "/content/cheatUser/register";
    }

    @PostMapping("/registerProc")
    public @ResponseBody String registerForm(@RequestBody CheatUserFormDto cheatUserFormDto){
        cheatUserService.register(cheatUserFormDto);

        return "register";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("form", new CheatUserFormDto());

        return "/content/cheatUser/login";
    }


    @PostMapping("/loginProc")
    public @ResponseBody
    String loginForm(CheatUserFormDto cheatUserFormDto){
        log.info(cheatUserFormDto.toString());
        cheatUserService.login(cheatUserFormDto);

        return "login";
    }
}
