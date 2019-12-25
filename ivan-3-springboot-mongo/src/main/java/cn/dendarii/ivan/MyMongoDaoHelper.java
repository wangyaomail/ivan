package cn.dendarii.ivan;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public abstract class MyMongoDaoHelper {
    @Autowired
    public MongoTemplate mongoTemplate;

    public abstract String getCollection();

    public void insert(Document doc) {
        mongoTemplate.insert(doc, getCollection());
    }

    public void delete(String id) {
        mongoTemplate.remove(new Document("_id", id), getCollection());
    }

    public void update(Document doc) {
        mongoTemplate.upsert(Query.query(Criteria.where("_id").is(doc.get("_id"))),
                             Update.fromDocument(doc),
                             getCollection());
    }

    public Document find(String id) {
        return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)),
                                     Document.class,
                                     getCollection());
    }
    

}
