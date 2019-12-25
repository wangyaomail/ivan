package cn.dendarii.ivan;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/")
    public String helloworld() {
        return "helloworld";
    }

}
