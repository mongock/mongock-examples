package io.mongock.professional.examples.changelogs.client.initializer;

import io.mongock.professional.examples.client.Client;
import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.bson.Document;

import static io.mongock.professional.examples.StandaloneMongoDbMultitenantApp.CLIENTS_COLLECTION_NAME;

@ChangeUnit(id="client-initializer", order = "1", author = "mongock")
public class ClientInitializerChangeLog {

  public final static int INITIAL_CLIENTS = 10;

  @Execution
  public void execution(MongoDatabase mongoDatabase) {
    
    mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME, Client.class)
            .insertMany(IntStream.range(0, INITIAL_CLIENTS)
                    .mapToObj(ClientInitializerChangeLog::getClient)
                    .collect(Collectors.toList()));
  }
  
  @RollbackExecution
  public void rollbackExecution(MongoDatabase mongoDatabase) {
      
      mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME, Client.class)
                   .deleteMany(new Document());
  }

  private static Client getClient(int i) {
    return new Client("name-" + i, "email-" + i, "phone" + i, "country" + i);
  }
}
