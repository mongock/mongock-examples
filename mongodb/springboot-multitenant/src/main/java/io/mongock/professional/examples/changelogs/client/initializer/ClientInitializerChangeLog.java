package io.mongock.professional.examples.changelogs.client.initializer;

import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import io.mongock.professional.examples.client.Client;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.mongodb.core.MongoTemplate;

import static io.mongock.professional.examples.SpringBootMultitenantApp.CLIENTS_COLLECTION_NAME;
import org.bson.Document;

@ChangeUnit(id="client-initializer", order = "1", author = "mongock")
public class ClientInitializerChangeLog {

  public final static int INITIAL_CLIENTS = 10;
  
  @BeforeExecution
  public void beforeExecution(MongoTemplate mongoTemplate) {
      
      mongoTemplate.createCollection(CLIENTS_COLLECTION_NAME);
  }
  
  @RollbackBeforeExecution
  public void rollbackBeforeExecution(MongoTemplate mongoTemplate) {
      
      mongoTemplate.dropCollection(CLIENTS_COLLECTION_NAME);
  }
  
  @Execution
  public void execution(MongoTemplate mongoTemplate) {
      
    mongoTemplate.insertAll(
            IntStream.range(0, INITIAL_CLIENTS)
                    .mapToObj(ClientInitializerChangeLog::getClient)
                    .collect(Collectors.toList())
    );
  }

  @RollbackExecution
  public void rollbackExecution(MongoTemplate mongoTemplate) {
      
      mongoTemplate.remove(new Document(), CLIENTS_COLLECTION_NAME);
  }

  private static Client getClient(int i) {
    return new Client("name-" + i, "email-" + i, "phone" + i, "country" + i);
  }
}
