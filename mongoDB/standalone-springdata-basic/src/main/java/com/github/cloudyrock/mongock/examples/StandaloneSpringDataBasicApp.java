package com.github.cloudyrock.mongock.examples;

import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import com.github.cloudyrock.mongock.examples.events.MongockEventListener;
import com.github.cloudyrock.standalone.MongockStandalone;
import com.github.cloudyrock.standalone.StandaloneRunner;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.MongoTemplate;

public class StandaloneSpringDataBasicApp {

  public final static String MONGODB_DB_NAME = "test";
  public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

  public static void main(String[] args) {
    getStandaloneRunner().execute();
  }

  private static StandaloneRunner getStandaloneRunner() {

    ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");

    MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build());

    MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGODB_DB_NAME);

    return MongockStandalone.builder()
            .setDriver(SpringDataMongoV3Driver.withDefaultLock(mongoTemplate))
            .addChangeLogsScanPackage("com.github.cloudyrock.mongock.examples.changelogs")
            .setMigrationStartedListener(MongockEventListener::onStart)
            .setMigrationSuccessListener(MongockEventListener::onSuccess)
            .setMigrationFailureListener(MongockEventListener::onFail)
            .buildRunner();
  }
}
