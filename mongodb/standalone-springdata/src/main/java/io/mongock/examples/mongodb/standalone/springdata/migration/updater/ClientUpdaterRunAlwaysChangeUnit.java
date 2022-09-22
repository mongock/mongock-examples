package io.mongock.examples.mongodb.standalone.springdata.migration.updater;

import io.mongock.examples.mongodb.standalone.springdata.StandaloneSpringdataApp;
import io.mongock.examples.mongodb.standalone.springdata.client.Client;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id="client-updater-runalways", order = "3", author = "mongock", runAlways = true)
public class ClientUpdaterRunAlwaysChangeUnit {

  @Execution
  public void execution(MongoTemplate mongoTemplate) {

    mongoTemplate.findAll(Client.class, StandaloneSpringdataApp.CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setCounter(client.getCounter() + 1))
            .forEach(client -> mongoTemplate.save(client, StandaloneSpringdataApp.CLIENTS_COLLECTION_NAME));
  }

  @RollbackExecution
  public void rollbackExecution(MongoTemplate mongoTemplate) {

    mongoTemplate.findAll(Client.class, StandaloneSpringdataApp.CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setCounter(client.getCounter() - 1))
            .forEach(client -> mongoTemplate.save(client, StandaloneSpringdataApp.CLIENTS_COLLECTION_NAME));
  }
}
