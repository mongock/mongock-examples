package io.mongock.examples.changelogs;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

@ChangeUnit(id= "add-basic-data", order = "2", author = "mongock")
public class ClientAddDataChange {

    @Execution
    public void execution(MongoDatabase mongoDatabase) {
        MongoCollection<Document> clients = mongoDatabase.getCollection("clients");
        clients.insertOne(new Document().append("name", "Antonio").append("surname", "perez"));
        clients.insertOne(new Document().append("name", "John").append("surname", "Wilson"));
    }

    @RollbackExecution
    public void rollback(MongoDatabase mongoDatabase) {
        MongoCollection<Document> clients = mongoDatabase.getCollection("clients");
        clients.deleteMany(new Document());
    }

}
