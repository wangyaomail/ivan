package cn.dendarii.ivan;

import java.util.Random;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @RequestMapping(value = "/test", method = { RequestMethod.GET })
    public String test() {
        return new Random().nextInt() + "";
    }
}
