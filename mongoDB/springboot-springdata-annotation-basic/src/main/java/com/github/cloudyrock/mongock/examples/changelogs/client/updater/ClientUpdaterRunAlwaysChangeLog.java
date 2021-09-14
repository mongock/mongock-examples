package com.github.cloudyrock.mongock.examples.changelogs.client.updater;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.github.cloudyrock.mongock.examples.client.Client;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.mongock.api.BasicChangeLog;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.stream.StreamSupport;

import static com.github.cloudyrock.mongock.examples.SpringBootSpringDataAnnotationBasicApp.CLIENTS_COLLECTION_NAME;

@ChangeLog(order = "3")
public class ClientUpdaterRunAlwaysChangeLog  implements BasicChangeLog {

  private final MongoTemplate mongoTemplate;

  public ClientUpdaterRunAlwaysChangeLog(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public String geId() {
    return "client-updater-runalways";
  }

  @Override
  public String getOrder() {
    return "3";
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
            .map(client -> client.setCounter(client.getCounter() + 1))
            .forEach(client -> mongoTemplate.save(client, CLIENTS_COLLECTION_NAME));
  }

  @Override
  public void rollback() {

    mongoTemplate.findAll(Client.class, CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setCounter(client.getCounter() - 1))
            .forEach(client -> mongoTemplate.save(client, CLIENTS_COLLECTION_NAME));
  }

}
