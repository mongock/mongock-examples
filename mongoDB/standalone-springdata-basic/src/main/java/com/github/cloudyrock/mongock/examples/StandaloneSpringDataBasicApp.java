package com.github.cloudyrock.mongock.examples;

import io.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import io.mongock.examples.events.MongockEventListener;
import io.mongock.runner.core.executor.MongockRunner;
import io.mongock.runner.standalone.MongockStandalone;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;

public class StandaloneSpringDataBasicApp {

  public final static String MONGODB_DB_NAME = "test";
  public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

  public static void main(String[] args) {
    getStandaloneRunner().execute();
  }

  private static MongockRunner getStandaloneRunner() {

    ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");

    MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build());

    MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGODB_DB_NAME);
    MongoTransactionManager mongoTransactionManager = new MongoTransactionManager(mongoTemplate.getMongoDbFactory());
    
    // Driver
    SpringDataMongoV3Driver driver = SpringDataMongoV3Driver.withDefaultLock(mongoTemplate);
    driver.enableTransactionWithTxManager(mongoTransactionManager);

    // Runner
    return MongockStandalone.builder()
            .setDriver(driver)
            .addChangeLogsScanPackage("io.mongock.examples.changelogs")
            .setMigrationStartedListener(MongockEventListener::onStart)
            .setMigrationSuccessListener(MongockEventListener::onSuccess)
            .setMigrationFailureListener(MongockEventListener::onFail)
            .setTrackIgnored(true)
            .setTransactionEnabled(true)
            .buildRunner();
  }
}
