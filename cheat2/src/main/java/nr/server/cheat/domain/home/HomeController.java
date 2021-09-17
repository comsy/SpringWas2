package nr.server.cheat.domain.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class HomeController {
    @RequestMapping(value = "/index", method=RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        List<String> resultList = new ArrayList<String>();
        resultList.add("Controller Add Text 1");
        mav.addObject("resultList",resultList);
        mav.setViewName("index");
        return mav;
    }

    @RequestMapping(value = "/index2", method=RequestMethod.GET)
    public ModelAndView index2(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        List<String> addText1 = new ArrayList<String>();
        addText1.add("Controller Add Text 1");

        List<String> addText2 = new ArrayList<String>();
        addText2.add("Controller Add Text 2");

        mav.addObject("key1", addText1);
        mav.addObject("key2", addText2);
        mav.setViewName("index2");
        return mav;
    }
}
