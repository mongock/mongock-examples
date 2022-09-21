package io.mongock.examples.mondogb.springboot.builder.migration;

import io.mongock.examples.mondogb.springboot.builder.BuilderApp;
import io.mongock.examples.mondogb.springboot.builder.client.Client;
import io.mongock.examples.mondogb.springboot.builder.client.ClientRepository;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id="client-initializer", order = "1", author = "mongock")
public class ClientInitializerChangeLog {

  public final static int INITIAL_CLIENTS = 10;
  
  @BeforeExecution
  public void beforeExecution(MongoTemplate mongoTemplate) {

      mongoTemplate.createCollection(BuilderApp.CLIENTS_COLLECTION_NAME);
  }
  
  @RollbackBeforeExecution
  public void rollbackBeforeExecution(MongoTemplate mongoTemplate) {
      
      mongoTemplate.dropCollection(BuilderApp.CLIENTS_COLLECTION_NAME);
  }

  @Execution
  public void execution(ClientRepository clientRepository) {
      
    clientRepository.saveAll(
            IntStream.range(0, INITIAL_CLIENTS)
                    .mapToObj(ClientInitializerChangeLog::getClient)
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
