package nr.server.cheat.domain.home;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {

    @GetMapping("/")
    public String home(){
        return "Hello Cheat.";
    }
}
