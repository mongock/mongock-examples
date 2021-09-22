package com.github.cloudyrock.mongock.examples.changelogs.client.updater;

import com.github.cloudyrock.mongock.examples.client.Client;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.github.cloudyrock.mongock.examples.SpringBootSpringDataAnnotationBasicApp.CLIENTS_COLLECTION_NAME;

@ChangeUnit(id="client-initializer-runalways", order = "3", author = "mongock_test", runAlways = true)
public class ClientUpdaterRunAlwaysChangeLog  {

  private final MongoTemplate mongoTemplate;

  public ClientUpdaterRunAlwaysChangeLog(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Execution
  public void changeSet() {

    mongoTemplate.findAll(Client.class, CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setCounter(client.getCounter() + 1))
            .forEach(client -> mongoTemplate.save(client, CLIENTS_COLLECTION_NAME));
  }

  @RollbackExecution
  public void rollback() {

    mongoTemplate.findAll(Client.class, CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setCounter(client.getCounter() - 1))
            .forEach(client -> mongoTemplate.save(client, CLIENTS_COLLECTION_NAME));
  }

}
