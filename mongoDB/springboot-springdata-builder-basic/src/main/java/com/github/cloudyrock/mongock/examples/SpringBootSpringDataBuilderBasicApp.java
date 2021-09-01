package com.github.cloudyrock.mongock.examples;

import io.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import com.github.cloudyrock.mongock.examples.client.ClientRepository;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ClientRepository.class)
public class SpringBootSpringDataBuilderBasicApp {

    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

    public static void main(String[] args) {
        getSpringAppBuilder().run(args);
    }

    private static SpringApplicationBuilder getSpringAppBuilder() {
        return new SpringApplicationBuilder().sources(SpringBootSpringDataBuilderBasicApp.class);
    }

    @Bean
    public MongockApplicationRunner mongockApplicationRunner(
            ApplicationContext springContext,
            MongoTemplate mongoTemplate,
            ApplicationEventPublisher eventPublisher,
            MongoTransactionManager mongoTransactionManager) {
      
      // Driver
      SpringDataMongoV3Driver driver = SpringDataMongoV3Driver.withDefaultLock(mongoTemplate);
      driver.enableTransactionWithTxManager(mongoTransactionManager);

      // Runner
      return MongockSpringboot.builder()
              .setDriver(driver)
              .addChangeLogsScanPackage("com.github.cloudyrock.mongock.examples.changelogs")
              .setSpringContext(springContext)
              .setEventPublisher(eventPublisher)
              .setTrackIgnored(true)
              .setTransactionEnabled(true)
              .buildApplicationRunner();
    }
    
    /**
     * Transaction Manager.
     * Needed to allow execution of changeSets in transaction scope.
     */
    @Bean
    public MongoTransactionManager transactionManager(MongoTemplate mongoTemplate) {
        return new MongoTransactionManager(mongoTemplate.getMongoDbFactory());
    }
}
