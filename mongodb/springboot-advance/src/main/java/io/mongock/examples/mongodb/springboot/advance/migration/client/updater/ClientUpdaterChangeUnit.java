package io.mongock.examples.mongodb.springboot.advance.migration.client.updater;

import io.mongock.examples.mongodb.springboot.advance.application.SpringBootAdvanceApp;
import io.mongock.examples.mongodb.springboot.advance.client.Client;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id="client-updater", order = "2", author = "mongock")
public class ClientUpdaterChangeUnit {

  @Execution
  public void execution(MongoTemplate mongoTemplate) {

    mongoTemplate.findAll(Client.class, SpringBootAdvanceApp.CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setName(client.getName() + "_updated"))
            .forEach(client -> mongoTemplate.save(client, SpringBootAdvanceApp.CLIENTS_COLLECTION_NAME));
  }

  @RollbackExecution
  public void rollbackExecution(MongoTemplate mongoTemplate) {
      
    mongoTemplate.findAll(Client.class, SpringBootAdvanceApp.CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setName(client.getName().substring(0, client.getName().length() - "_updated".length())))
            .forEach(client -> mongoTemplate.save(client, SpringBootAdvanceApp.CLIENTS_COLLECTION_NAME));

  }
}
