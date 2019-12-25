package cn.dendarii.ivan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import cn.dendarii.ivan.trie.Forest;
import cn.dendarii.ivan.trie.GetWord;
import cn.dendarii.ivan.trie.Library;
import cn.dendarii.ivan.trie.NlpWord;

@Service
public class RobotService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Resource
    private WordDicDao wordDicDao;

    public Forest forest;

    @PostConstruct
    public void init() {
        List<Document> words = mongoTemplate.findAll(Document.class, wordDicDao.getCollection());

        List<NlpWord> nlpWords = new LinkedList<>();
        for (Document w : words) {
            String name = w.getString("_id");
            String nature = w.getString("nature");
            if (name.length() > 1 && (nature.contains("n") || nature.contains("v"))) {
                NlpWord word = new NlpWord();
                word.setName(name);
                word.setNature(nature);
                nlpWords.add(word);
            }
        }
        try {
            forest = Library.makeForest(nlpWords);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String genAnswer(String askStr) {
        GetWord udg = forest.getWord(askStr);
        ArrayList<String> words = new ArrayList<>();
        String temp = null;
        while ((temp = udg.getAllWords()) != null) {
            Document word = wordDicDao.find(temp);
            if (word.getString("nature").equals("nnt")) {
                return temp + "是谁？";
            } else {
                words.add(temp);
            }
        }
        return words.toString();
    }

    String[] answerPlat = { "没听懂", "你说的什么？", "请再说一下" };

    public String genBlankAnswer() {
        Random rand = new Random();
        return answerPlat[Math.abs(rand.nextInt(answerPlat.length))];
    }

}
