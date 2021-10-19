package io.mongock.professional.examples;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.mongock.professional.driver.mongodb.springdata.v3.SpringDataMongoV3DriverProfessional;
import io.mongock.professional.examples.spring.DateToZonedDateTimeConverter;
import io.mongock.professional.examples.spring.ZonedDateTimeToDateConverter;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockApplicationRunner;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@SpringBootApplication
public class SpringBootSpringDataBuilderMultitenantApp {

    public final static String MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/";
    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

    public static void main(String[] args) {
        getSpringAppBuilder().run(args);
    }

    private static SpringApplicationBuilder getSpringAppBuilder() {
        return new SpringApplicationBuilder().sources(SpringBootSpringDataBuilderMultitenantApp.class);
    }

    @Bean
    public MongockApplicationRunner mongockApplicationRunner(
            ApplicationContext springContext,
            ApplicationEventPublisher eventPublisher) {
      
      // Custom conversions for mongo templates
      MongoCustomConversions customConversions = this.getCustomConversions();
      
      // Drivers
      MongoTemplate mongoTemplate1 = this.generateMongoTemplate(MONGODB_CONNECTION_STRING, "test1", customConversions);
      SpringDataMongoV3DriverProfessional driver1 = SpringDataMongoV3DriverProfessional.withDefaultLock(mongoTemplate1);
      MongoTransactionManager transactionManager1 = new MongoTransactionManager(mongoTemplate1.getMongoDbFactory());
      driver1.enableTransactionWithTxManager(transactionManager1);
      
      MongoTemplate mongoTemplate2 = this.generateMongoTemplate(MONGODB_CONNECTION_STRING, "test2", customConversions);
      SpringDataMongoV3DriverProfessional driver2 = SpringDataMongoV3DriverProfessional.withDefaultLock(mongoTemplate2);
      MongoTransactionManager transactionManager2 = new MongoTransactionManager(mongoTemplate2.getMongoDbFactory());
      driver2.enableTransactionWithTxManager(transactionManager2);
      
      MongoTemplate mongoTemplate3 = this.generateMongoTemplate(MONGODB_CONNECTION_STRING, "test3", customConversions);
      SpringDataMongoV3DriverProfessional driver3 = SpringDataMongoV3DriverProfessional.withDefaultLock(mongoTemplate3);
      MongoTransactionManager transactionManager3 = new MongoTransactionManager(mongoTemplate3.getMongoDbFactory());
      driver3.enableTransactionWithTxManager(transactionManager3);

      // Runner
      return MongockSpringboot.builder()
              .setDriver(
                      SpringDataMongoV3DriverProfessional.multiTenant()
                            .addTenant(driver1)
                            .addTenant(driver2)
                            .addTenant(driver3)
                        )
              .addMigrationScanPackage("io.mongock.professional.examples.changelogs")
              .setSpringContext(springContext)
              .setEventPublisher(eventPublisher)
              .setTrackIgnored(true)
              .setTransactionEnabled(true)
              .buildApplicationRunner();
    }
        
    private MongoTemplate generateMongoTemplate(String connectionString, String dbName, MongoCustomConversions customConversions) {
      
      MongoClient mongoClient = MongoClients.create(connectionString);
      MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, dbName);
      
      // Custom converters to map ZonedDateTime.
      MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
      mongoMapping.setCustomConversions(customConversions);
      mongoMapping.afterPropertiesSet();
      
      return mongoTemplate;
    }
    
    /**
     * Custom converters to map ZonedDateTime.
     */
    private MongoCustomConversions getCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(DateToZonedDateTimeConverter.INSTANCE);
        converters.add(ZonedDateTimeToDateConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }
}
