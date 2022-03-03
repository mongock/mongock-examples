package io.mongock.examples.changelogs;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import io.mongock.examples.RemoteClientService;
import org.bson.Document;

import static io.mongock.examples.QuickStartApp.CLIENTS_COLLECTION_NAME;

@ChangeUnit(id = "client-add-remote", order = "3", author = "mongock")
public class ClientAddRemoteChange {

    @Execution
    public void execution(MongoDatabase mongoDatabase, RemoteClientService remoteClientService) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME);
        for(Document remoteDocument : remoteClientService.getClientsFromRemoteServer()) {
            collection.insertOne(remoteDocument);
        }
    }

    @RollbackExecution
    public void rollbackExecution(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME).deleteMany(new Document("type", "remote"));

    }

}
