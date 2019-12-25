package cn.dendarii.ivan.api.qa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.dendarii.ivan.api.content.bean.http.HBArticleContent;
import cn.dendarii.ivan.api.qa.bean.http.PoetryAnswer;
import cn.dendarii.ivan.api.qa.service.QaService;
import cn.dendarii.ivan.api.web.controller.BaseController;
import cn.dendarii.ivan.common.bean.anno.RegisterMethod;
import cn.dendarii.ivan.common.bean.http.ResponseBean;
import cn.dendarii.ivan.util.id.IDUtil;
import cn.dendarii.ivan.util.set.HBCollectionUtil;
import cn.dendarii.ivan.util.set.HBStringUtil;
import cn.dendarii.ivan.util.set.RandomUtils;

@RestController
@RequestMapping(value = { "/${api.version}/b/qa" })
public class QaBController extends BaseController {
    @Autowired
    private QaService qaService;

    @Override
    @RequestMapping(value = "/{func}", method = { RequestMethod.GET })
    public ResponseBean man(@PathVariable String func) {
        return super.man(func);
    }

    @RegisterMethod
    public PoetryAnswer poetry() {
        PoetryAnswer answer = new PoetryAnswer();
        String ask = request.getParameter("ask");
        if (HBStringUtil.isNotBlank(ask)) {
            ask = ask.trim();
            answer.setId(IDUtil.generateRandomKey());
            answer.setAvatar("https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar");
            answer.setTitle("机器人说：");
            if (ask.startsWith("来一首")) {
                if (ask.endsWith("的诗")) {
                    String author = ask.substring(3, ask.length() - 2);
                    System.out.println(author);
                    List<HBArticleContent> poetries = mongoTemplate.find(Query.query(Criteria.where("author")
                                                                                             .is(author)),
                                                                         HBArticleContent.class);
                    if (HBCollectionUtil.isNotEmpty(poetries)) {
                        HBArticleContent content = RandomUtils.randomItem(poetries);
                        answer.setAnswer("《" + content.getTitle() + "》\\n" + content.getContent());
                    }
                } else if (ask.startsWith("来一首诗，")) {
                    String[] toks = ask.split("要有");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < toks.length; i++) {
                        sb.append(toks[i]);
                        System.out.println(toks[i]);
                    }
                    List<HBArticleContent> poetries = mongoTemplate.find(Query.query(Criteria.where("content")
                                                                                             .regex("["
                                                                                                     + sb.toString()
                                                                                                     + "]")),
                                                                         HBArticleContent.class);
                    if (HBCollectionUtil.isNotEmpty(poetries)) {
                        HBArticleContent content = RandomUtils.randomItem(poetries);
                        answer.setAnswer("《" + content.getTitle() + "》\\n" + content.getContent());
                    }
                } else {
                    String category = ask.substring(3);
                    System.out.println(category);
                    List<HBArticleContent> poetries = mongoTemplate.find(Query.query(Criteria.where("categorys")
                                                                                             .in(category)),
                                                                         HBArticleContent.class);
                    if (HBCollectionUtil.isNotEmpty(poetries)) {
                        HBArticleContent content = RandomUtils.randomItem(poetries);
                        answer.setAnswer("《" + content.getTitle() + "》\\n" + content.getContent());
                    }
                }
            }
        }
        return answer;
    }
}
