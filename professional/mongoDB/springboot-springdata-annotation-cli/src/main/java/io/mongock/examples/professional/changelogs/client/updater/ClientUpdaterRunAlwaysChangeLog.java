package io.mongock.examples.professional.changelogs.client.updater;

import io.mongock.examples.professional.client.Client;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import io.mongock.examples.professional.App;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id="client-updater-runalways", order = "3", author = "mongock", runAlways = true)
public class ClientUpdaterRunAlwaysChangeLog  {

  @Execution
  public void execution(MongoTemplate mongoTemplate) {

    mongoTemplate.findAll(Client.class, App.CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setCounter(client.getCounter() + 1))
            .forEach(client -> mongoTemplate.save(client, App.CLIENTS_COLLECTION_NAME));
  }

  @RollbackExecution
  public void rollbackExecution(MongoTemplate mongoTemplate) {

    mongoTemplate.findAll(Client.class, App.CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setCounter(client.getCounter() - 1))
            .forEach(client -> mongoTemplate.save(client, App.CLIENTS_COLLECTION_NAME));
  }
}
