package io.mongock.professional.examples.mongodb.springboot.multitenant.migration;

import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

import io.mongock.professional.examples.mongodb.springboot.multitenant.client.Client;
import io.mongock.professional.examples.mongodb.springboot.multitenant.client.ClientRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.mongock.professional.examples.mongodb.springboot.multitenant.SpringBootMultitenantApp.CLIENTS_COLLECTION_NAME;


@ChangeUnit(id="client-initializer", order = "1", author = "mongock")
public class ClientInitializerChangeUnit {

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
