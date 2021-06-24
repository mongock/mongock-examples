package com.github.cloudyrock.mongock.examples;

import com.github.cloudyrock.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import com.github.cloudyrock.mongock.examples.codec.ZonedDateTimeCodec;
import com.github.cloudyrock.mongock.examples.events.MongockEventListener;
import com.github.cloudyrock.mongock.runner.core.executor.MongockRunner;
import com.github.cloudyrock.standalone.MongockStandalone;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class StandaloneMongoDbAdvancedApp {

  public final static String MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/";
  public final static String MONGODB_MAIN_DB_NAME = "test";

  public final static String CLIENTS_COLLECTION_NAME = "clientCollection";
  public final static String PRODUCTS_COLLECTION_NAME = "productCollection";

  public static void main(String[] args) {
    getStandaloneRunner().execute();
  }

  private static MongockRunner getStandaloneRunner() {

    return MongockStandalone.builder()
                            .setDriver(MongoSync4Driver.withDefaultLock(getMainMongoClient(), MONGODB_MAIN_DB_NAME))
                            .addChangeLogsScanPackage("com.github.cloudyrock.mongock.examples.changelogs")
                            .setMigrationStartedListener(MongockEventListener::onStart)
                            .setMigrationSuccessListener(MongockEventListener::onSuccess)
                            .setMigrationFailureListener(MongockEventListener::onFail)
                            .addDependency("secondaryDb", getSecondaryDb())
                            .setTrackIgnored(true)
                            .setTransactionEnabled(true)
                            .buildRunner();
  }

  /**
   * Main MongoClient for Mongock to work.
   */
  private static MongoClient getMainMongoClient() {
    return buildMongoClientWithCodecs(MONGODB_CONNECTION_STRING);
  }

  /**
   * Adding MongoDatabase dependency we can access mongodb secondary databases.
   * Secondary database should be used only for read, because it won't be
   * managed by Mongock. We can inject it in ChangeSets this way:
   * @Named("secondaryDb") @NonLockGuarded MongoDatabase secondaryDb In this
   * example we are connecting to a single mongodb server, but we could connect
   * to a different server in the same way.
   */
  private static MongoDatabase getSecondaryDb() {
    MongoClient mongoClient = buildMongoClientWithCodecs(MONGODB_CONNECTION_STRING);
    return mongoClient.getDatabase("secondaryDb");
  }

  /**
   * Helper to create MongoClients customized including Codecs
   */
  private static MongoClient buildMongoClientWithCodecs(String connectionString) {

    CodecRegistry codecRegistry = fromRegistries(fromCodecs(new ZonedDateTimeCodec()),
                                                  MongoClientSettings.getDefaultCodecRegistry(), 
                                                  fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    return MongoClients.create(MongoClientSettings.builder()
                                                  .applyConnectionString(new ConnectionString(connectionString))
                                                  .codecRegistry(codecRegistry)
                                                  .build());
  }
}
