package io.mongock.examples.changelogs.client.initializer;

import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

import io.mongock.examples.client.Client;
import com.mongodb.client.MongoDatabase;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.bson.Document;

import static io.mongock.examples.StandaloneMongoDbAdvancedApp.CLIENTS_COLLECTION_NAME;

@ChangeUnit(id="client-initializer", order = "1", author = "mongock")
public class ClientInitializerChangeLog {

  public final static int INITIAL_CLIENTS = 10;
  
  @BeforeExecution
  public void beforeExecution(MongoDatabase mongoDatabase) {
      
      mongoDatabase.createCollection(CLIENTS_COLLECTION_NAME);
  }
  
  @RollbackBeforeExecution
  public void rollbackBeforeExecution(MongoDatabase mongoDatabase) {
      
      mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME).drop();
  }

  @Execution
  public void execution(MongoDatabase mongoDatabase) {
    
    mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME, Client.class)
            .insertMany(IntStream.range(0, INITIAL_CLIENTS)
                    .mapToObj(ClientInitializerChangeLog::getClient)
                    .collect(Collectors.toList()));
  }
  
  @RollbackExecution
  public void rollbackExecution(MongoDatabase mongoDatabase) {
    mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME, Client.class).deleteMany(new Document());
  }

  private static Client getClient(int i) {
    return new Client("name-" + i, "email-" + i, "phone" + i, "country" + i);
  }
}
