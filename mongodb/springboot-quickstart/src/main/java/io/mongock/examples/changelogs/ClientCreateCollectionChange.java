package io.mongock.examples.changelogs;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

import static io.mongock.examples.QuickStartApp.CLIENTS_COLLECTION_NAME;

@ChangeUnit(id = "client-create-collection", order = "1", author = "mongock", transactional = false)
public class ClientCreateCollectionChange {

    @Execution
    public void execution(MongoDatabase mongoDatabase) {
        mongoDatabase.createCollection(CLIENTS_COLLECTION_NAME);
        mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME).createIndex(Indexes.ascending("name"));
    }

    @RollbackExecution
    public void rollbackExecution(ClientSession clientSession, MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME).drop();
    }

}
