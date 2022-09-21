package io.mongock.examples.mongodb.standalone.springdata.migration.initializer;

import io.mongock.examples.mongodb.standalone.springdata.StandaloneSpringdataApp;
import io.mongock.examples.mongodb.standalone.springdata.client.Client;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id="client-initializer", order = "1", author = "mongock")
public class ClientInitializerChangeUnit {

  public final static int INITIAL_CLIENTS = 10;
  
  @BeforeExecution
  public void beforeExecution(MongoTemplate mongoTemplate) {
      
      mongoTemplate.createCollection(StandaloneSpringdataApp.CLIENTS_COLLECTION_NAME);
  }
  
  @RollbackBeforeExecution
  public void rollbackBeforeExecution(MongoTemplate mongoTemplate) {
      
      mongoTemplate.dropCollection(StandaloneSpringdataApp.CLIENTS_COLLECTION_NAME);
  }

  @Execution
  public void execution(MongoTemplate mongoTemplate) {
      
    mongoTemplate.insertAll(
            IntStream.range(0, INITIAL_CLIENTS)
                    .mapToObj(ClientInitializerChangeUnit::getClient)
                    .collect(Collectors.toList())
    );
  }

  @RollbackExecution
  public void rollbackExecution(MongoTemplate mongoTemplate) {
      
    mongoTemplate.remove(new Document(), StandaloneSpringdataApp.CLIENTS_COLLECTION_NAME);
  }

  private static Client getClient(int i) {
    return new Client("name-" + i, "email-" + i, "phone" + i, "country" + i);
  }
}
