package com.github.cloudyrock.mongock.examples.changelogs.client.updater;

import com.github.cloudyrock.mongock.examples.client.Client;
import io.mongock.api.BasicChangeLog;
import io.mongock.api.ChangeLogInfo;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.github.cloudyrock.mongock.examples.SpringBootSpringDataAnnotationBasicApp.CLIENTS_COLLECTION_NAME;

@ChangeLogInfo(id="client-initializer", order = "2", author = "mongock_test", runAlways = true)
public class ClientUpdaterChangeLog  implements BasicChangeLog {

  private final MongoTemplate mongoTemplate;

  public ClientUpdaterChangeLog(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void changeSet() {

    mongoTemplate.findAll(Client.class, CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setName(client.getName() + "_updated"))
            .forEach(client -> mongoTemplate.save(client, CLIENTS_COLLECTION_NAME));
  }

  @Override
  public void rollback() {
    mongoTemplate.findAll(Client.class, CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setName(client.getName().substring(0, client.getName().length() - "_updated".length())))
            .forEach(client -> mongoTemplate.save(client, CLIENTS_COLLECTION_NAME));

  }

}
