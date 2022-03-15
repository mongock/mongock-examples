package io.mongock.examples.changelogs;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id= "crate-collection", order = "1", author = "mongock", transactional = false)
public class ClientCreateCollectionChange {

    @Execution
    public void execution(MongoDatabase mongoDatabase) {
        mongoDatabase.createCollection("clients");
        mongoDatabase.getCollection("clients").createIndex(Indexes.ascending("name"));
    }

    @RollbackExecution
    public void rollback(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection("clients").drop();
    }

}
