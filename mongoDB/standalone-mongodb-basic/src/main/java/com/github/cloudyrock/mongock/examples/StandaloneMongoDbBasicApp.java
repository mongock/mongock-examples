package com.github.cloudyrock.mongock.examples;

import com.github.cloudyrock.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import com.github.cloudyrock.mongock.examples.codec.ZonedDateTimeCodec;
import com.github.cloudyrock.mongock.examples.events.MongockEventListener;
import com.github.cloudyrock.standalone.MongockStandalone;
import com.github.cloudyrock.standalone.StandaloneRunner;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class StandaloneMongoDbBasicApp {

  public final static String MONGODB_DB_NAME = "test";
  public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

  public static void main(String[] args) {
    getStandaloneRunner().execute();
  }

  private static StandaloneRunner getStandaloneRunner() {

    ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
    CodecRegistry codecRegistry = fromRegistries(fromCodecs(new ZonedDateTimeCodec()),
                                                  MongoClientSettings.getDefaultCodecRegistry(), 
                                                  fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
                                                  .applyConnectionString(connectionString)
                                                  .codecRegistry(codecRegistry)
                                                  .build());

    return MongockStandalone.builder()
                            .setDriver(MongoSync4Driver.withDefaultLock(mongoClient, MONGODB_DB_NAME))
                            .addChangeLogsScanPackage("com.github.cloudyrock.mongock.examples.changelogs")
                            .setMigrationStartedListener(MongockEventListener::onStart)
                            .setMigrationSuccessListener(MongockEventListener::onSuccess)
                            .setMigrationFailureListener(MongockEventListener::onFail)
                            .buildRunner();
  }
}
