package com.github.cloudyrock.mongock.examples.changelogs.client.updater;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.examples.client.Client;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.mongock.api.BasicChangeLog;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.github.cloudyrock.mongock.examples.SpringBootSpringDataAnnotationBasicApp.CLIENTS_COLLECTION_NAME;

public class ClientUpdaterChangeLog  implements BasicChangeLog {

  private final MongoTemplate mongoTemplate;

  public ClientUpdaterChangeLog(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public String geId() {
    return "client-updater";
  }

  @Override
  public String getOrder() {
    return "2";
  }

  @Override
  public String getAuthor() {
    return "mongock";
  }

  @Override
  public boolean isRunAlways() {
    return true;
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
