package io.mongock.examples.changelogs;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import io.mongock.examples.RemoteClientService;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id= "add-remote-data", order = "3", author = "mongock")
public class ClientAddRemoteDataChange {

    @Execution
    public void execution(MongoDatabase mongoDatabase, RemoteClientService remoteClientService) {
        MongoCollection<Document> clients = mongoDatabase.getCollection("clients");

        for(Document doc : remoteClientService.getClientsFromRemoteServer()) {
            clients.insertOne(doc);
        }
    }

    @RollbackExecution
    public void rollback(MongoDatabase mongoDatabase) {
        MongoCollection<Document> clients = mongoDatabase.getCollection("clients");
        clients.deleteMany(new Document().append("type", "remote"));
    }

}
