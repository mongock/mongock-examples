package io.mongock.professional.examples;

import io.mongock.professional.examples.events.MongockEventListener;
import io.mongock.professional.driver.mongodb.sync.v4.driver.MongoSync4DriverProfessional;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.mongock.runner.core.executor.MongockRunner;
import io.mongock.runner.standalone.MongockStandalone;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class StandaloneMongoDbMultitenantApp {

  public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

  public static void main(String[] args) {
    getStandaloneRunner().execute();
  }

  private static MongockRunner getStandaloneRunner() {

    MongoClient mongoClient = buildMongoClient();

    return MongockStandalone.builder()
                            .setDriver(
                                    MongoSync4DriverProfessional.multiTenant()
                                          .addTenant(MongoSync4DriverProfessional.withDefaultLock(mongoClient, "test1"))
                                          .addTenant(MongoSync4DriverProfessional.withDefaultLock(mongoClient, "test2"))
                                          .addTenant(MongoSync4DriverProfessional.withDefaultLock(mongoClient, "test3"))
                                      )
                            .addMigrationScanPackage("io.mongock.professional.examples.changelogs")
                            .setMigrationStartedListener(MongockEventListener::onStart)
                            .setMigrationSuccessListener(MongockEventListener::onSuccess)
                            .setMigrationFailureListener(MongockEventListener::onFail)
                            .setTrackIgnored(true)
                            .setTransactionEnabled(true)
                            .buildRunner();
  }
  
  private static MongoClient buildMongoClient() {
    ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
    CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
                                                  fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    return MongoClients.create(MongoClientSettings.builder()
                                                  .applyConnectionString(connectionString)
                                                  .codecRegistry(codecRegistry)
                                                  .build());
  }
}
