package io.mongock.examples.changelogs;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

import static io.mongock.examples.QuickStartApp.CLIENTS_COLLECTION_NAME;

@ChangeUnit(id = "client-add-data", order = "2", author = "mongock")
public class ClientAddDataChange {

    @Execution
    public void execution(MongoDatabase mongoDatabase) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME);
        collection.insertOne(new Document().append("name", "James").append("surname", "Johnson"));
        collection.insertOne(new Document().append("name", "Peter").append("surname", "Sues"));
        collection.insertOne(new Document().append("name", "Claudia").append("surname", "Diaz"));


    }

    @RollbackExecution
    public void rollbackExecution(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME).deleteMany(new Document());

    }

}
