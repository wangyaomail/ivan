package cn.dendarii.ivan;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping(value = { "/one" })
public class ControllerOne {
    @Autowired
    private RobotService robotService;

    @Autowired
    protected HttpServletRequest request;

    @RequestMapping(value = "/fullfilldic", method = { RequestMethod.GET })
    public Document fullfilldic() {
        return null;
    }

    @RequestMapping(value = "/answer", method = { RequestMethod.GET, RequestMethod.POST })
    public JSONObject answer() {
        String ask = request.getParameter("ask");
        JSONObject result = new JSONObject();
        if (ask != null) {
            result.put("ans", robotService.genAnswer(ask));
        } else
            result.put("ans", robotService.genBlankAnswer());
        return result;
    }

    @RequestMapping(value = "/login", method = { RequestMethod.POST })
    public JSONObject login(String username,
                            String password) {
        JSONObject result = new JSONObject();
        System.out.println(username);
        System.out.println(password);
        return result;
    }

    @RequestMapping(value = "/get_info", method = { RequestMethod.GET })
    public JSONObject getInfo() {
        JSONObject result = JSONObject.parseObject("{name:'super_admin',user_id:'1',access:['super_admin','admin'],token:'super_admin',avatar:'https://file.iviewui.com/dist/a0e88e83800f138b94d2414621bd9704.png'}");
        return result;
    }
}
