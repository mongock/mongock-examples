package io.mongock.examples.mongodb.springboot.quickstart.migration;

import io.mongock.examples.mongodb.springboot.quickstart.QuickStartApp;
import io.mongock.examples.mongodb.springboot.quickstart.client.Client;
import io.mongock.examples.mongodb.springboot.quickstart.client.ClientRepository;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id="client-initializer", order = "1", author = "mongock")
public class ClientInitializerChangeUnit {

  public final static int INITIAL_CLIENTS = 10;
  
  @BeforeExecution
  public void beforeExecution(MongoTemplate mongoTemplate) {

      mongoTemplate.createCollection(QuickStartApp.CLIENTS_COLLECTION_NAME);
  }
  
  @RollbackBeforeExecution
  public void rollbackBeforeExecution(MongoTemplate mongoTemplate) {
      
      mongoTemplate.dropCollection(QuickStartApp.CLIENTS_COLLECTION_NAME);
  }

  @Execution
  public void execution(ClientRepository clientRepository) {
      
    clientRepository.saveAll(
            IntStream.range(0, INITIAL_CLIENTS)
                    .mapToObj(ClientInitializerChangeUnit::getClient)
                    .collect(Collectors.toList())
    );
  }

  @RollbackExecution
  public void rollbackExecution(ClientRepository clientRepository) {
      
    clientRepository.deleteAll();
  }

  private static Client getClient(int i) {
    return new Client("name-" + i, "email-" + i, "phone" + i, "country" + i);
  }
}
