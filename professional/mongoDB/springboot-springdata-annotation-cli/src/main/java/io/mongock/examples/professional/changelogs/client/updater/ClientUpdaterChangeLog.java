package io.mongock.examples.professional.changelogs.client.updater;

import io.mongock.examples.professional.client.Client;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import io.mongock.examples.professional.App;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id="client-updater", order = "2", author = "mongock")
public class ClientUpdaterChangeLog  {

  @Execution
  public void execution(MongoTemplate mongoTemplate) {

    mongoTemplate.findAll(Client.class, App.CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setName(client.getName() + "_updated"))
            .forEach(client -> mongoTemplate.save(client, App.CLIENTS_COLLECTION_NAME));
  }

  @RollbackExecution
  public void rollbackExecution(MongoTemplate mongoTemplate) {
      
    mongoTemplate.findAll(Client.class, App.CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setName(client.getName().substring(0, client.getName().length() - "_updated".length())))
            .forEach(client -> mongoTemplate.save(client, App.CLIENTS_COLLECTION_NAME));

  }
}
