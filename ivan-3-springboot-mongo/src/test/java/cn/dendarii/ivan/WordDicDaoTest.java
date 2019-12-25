package cn.dendarii.ivan;

import java.io.BufferedReader;
import java.io.FileReader;

import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class WordDicDaoTest {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = SpringApplication.run(AppStarter.class, args);
        WordDicDao wordDicDao = (WordDicDao) applicationContext.getBean("wordDicDao");
        BufferedReader br = new BufferedReader(new FileReader("D:\\data\\word2vec\\default.dic"));
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] toks = line.split("\t");
            if (toks.length == 3) {
                Document doc = new Document();
                doc.put("_id", toks[0]);
                doc.put("nature", toks[1]);
                wordDicDao.insert(doc);
            }
        }
        br.close();
        System.exit(0);
    }

}
