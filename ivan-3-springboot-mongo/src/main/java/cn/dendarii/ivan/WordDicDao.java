package cn.dendarii.ivan;

import org.springframework.stereotype.Repository;

@Repository("wordDicDao")
public class WordDicDao extends MyMongoDaoHelper {

    @Override
    public String getCollection() {
        return "word";
    }

}
