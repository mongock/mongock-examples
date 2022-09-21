package io.mongock.examples.mongodb.springboot.advance.migration.client.initializer;

import io.mongock.examples.mongodb.springboot.advance.application.SpringBootAdvanceApp;
import io.mongock.examples.mongodb.springboot.advance.client.Client;
import io.mongock.examples.mongodb.springboot.advance.client.ClientRepository;
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
      
      mongoTemplate.createCollection(SpringBootAdvanceApp.CLIENTS_COLLECTION_NAME);
  }
  
  @RollbackBeforeExecution
  public void rollbackBeforeExecution(MongoTemplate mongoTemplate) {
      
      mongoTemplate.dropCollection(SpringBootAdvanceApp.CLIENTS_COLLECTION_NAME);
  }

  @Execution
  public void execution(ClientRepository clientRepository, MongoTemplate mongoTemplate) {
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
