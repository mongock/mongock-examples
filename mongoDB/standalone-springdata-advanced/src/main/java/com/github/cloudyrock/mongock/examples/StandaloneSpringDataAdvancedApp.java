package com.github.cloudyrock.mongock.examples;

import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import com.github.cloudyrock.mongock.examples.events.MongockEventListener;
import com.github.cloudyrock.mongock.examples.spring.DateToZonedDateTimeConverter;
import com.github.cloudyrock.mongock.examples.spring.ZonedDateTimeToDateConverter;
import com.github.cloudyrock.standalone.MongockStandalone;
import com.github.cloudyrock.standalone.StandaloneRunner;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

public class StandaloneSpringDataAdvancedApp {

  public final static String MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/";
  public final static String MONGODB_MAIN_DB_NAME = "test";

  public final static String CLIENTS_COLLECTION_NAME = "clientCollection";
  public final static String PRODUCTS_COLLECTION_NAME = "productCollection";

  public static void main(String[] args) {
    getStandaloneRunner().execute();
  }

  private static StandaloneRunner getStandaloneRunner() {

    return MongockStandalone.builder()
            .setDriver(SpringDataMongoV3Driver.withDefaultLock(getMainMongoTemplate()))
            .addChangeLogsScanPackage("com.github.cloudyrock.mongock.examples.changelogs")
            .setMigrationStartedListener(MongockEventListener::onStart)
            .setMigrationSuccessListener(MongockEventListener::onSuccess)
            .setMigrationFailureListener(MongockEventListener::onFail)
            .addDependency("secondaryDb", getSecondaryDb())
            .addDependency("secondaryMongoTemplate", getSecondaryMongoTemplate())
            .buildRunner();
  }

  /**
   * Main MongoTemplate for Mongock to work.
   */
  private static MongoTemplate getMainMongoTemplate() {

    MongoClient mongoClient = MongoClients.create(MONGODB_CONNECTION_STRING);
    MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGODB_MAIN_DB_NAME);

    // Custom converters to map ZonedDateTime.
    MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
    mongoMapping.setCustomConversions(customConversions());
    mongoMapping.afterPropertiesSet();

    return mongoTemplate;
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
   * Also we can add MongoTemplate as a dependency to access mongodb secondary
   * databases. Secondary template should be used only for read, because it
   * won't be managed by Mongock. We can inject it in ChangeSets this way:
   * @Named("secondaryMongoTemplate") @NonLockGuarded MongoTemplate
   * secondaryTemplate In this example we are connecting to a single mongodb
   * server, but we could connect to a different server in the same way.
   */
  private static MongoTemplate getSecondaryMongoTemplate() {

    MongoClient mongoClient = MongoClients.create(MONGODB_CONNECTION_STRING);
    MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "secondaryDb");

    // Custom converters to map ZonedDateTime.
    MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
    mongoMapping.setCustomConversions(customConversions());
    mongoMapping.afterPropertiesSet();

    return mongoTemplate;
  }

  /**
   * Custom converters to map ZonedDateTime.
   */
  private static MongoCustomConversions customConversions() {
    List<Converter<?, ?>> converters = new ArrayList<>();
    converters.add(DateToZonedDateTimeConverter.INSTANCE);
    converters.add(ZonedDateTimeToDateConverter.INSTANCE);
    return new MongoCustomConversions(converters);
  }

  /**
   * Helper to create MongoClients customized including Codecs
   */
  private static MongoClient buildMongoClientWithCodecs(String connectionString) {

    CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    return MongoClients.create(MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .codecRegistry(codecRegistry)
            .build());
  }
}
