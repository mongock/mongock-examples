package com.github.cloudyrock.mongock.examples;

import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import com.github.cloudyrock.mongock.examples.client.ClientRepository;
import com.github.cloudyrock.springboot.MongockSpringboot;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
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
    public MongockSpringboot.MongockApplicationRunner mongockApplicationRunner(
            ApplicationContext springContext,
            MongoTemplate mongoTemplate,
            ApplicationEventPublisher eventPublisher) {

        return MongockSpringboot.builder()
                .setDriver(SpringDataMongoV3Driver.withDefaultLock(mongoTemplate))
                .addChangeLogsScanPackage("com.github.cloudyrock.mongock.examples.changelogs")
                .setSpringContext(springContext)
                .setEventPublisher(eventPublisher)
                .setTrackIgnored(true)
                .buildApplicationRunner();
    }
}
