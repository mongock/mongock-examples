package com.github.cloudyrock.mongock;


import com.github.cloudyrock.mongock.client.ClientRepository;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import com.github.cloudyrock.mongock.spring.DateToZonedDateTimeConverter;
import com.github.cloudyrock.mongock.spring.ZonedDateTimeToDateConverter;
import com.github.cloudyrock.spring.v5.MongockSpring5;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ClientRepository.class)
public class Spring5SpringData3MultipleTemplatesApp {

    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

    public static void main(String[] args) {
        getSpringAppBuilder().run(args);
    }


    public static SpringApplicationBuilder getSpringAppBuilder() {
        return new SpringApplicationBuilder().sources(Spring5SpringData3MultipleTemplatesApp.class);
    }

    @Bean
    public MongoClient mongoClient(){
        return MongoClients.create("mongodb://localhost:27017/test&readPreference=primary");
    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "test");
    }

    @Bean
    public MongoTemplate mongoTemplateWrong(MongoClient mongoClient) {
        return new MongoTemplate(
                MongoClients.create("mongodb://localhost:27017/wrong&readPreference=primary"),
                "test");
    }

    @Bean
    public MongockSpring5.MongockApplicationRunner mongockApplicationRunner(
            ApplicationContext springContext,
            @Qualifier("mongoTemplate") MongoTemplate mongoTemplate,
            ApplicationEventPublisher eventPublisher) {

        if(!"test".equals(mongoTemplate.getDb().getName())) {
            throw new RuntimeException("Wrong MongoTemplate injected to MongockRunner");
        }
        return MongockSpring5.builder()
                .setDriver(SpringDataMongoV3Driver.withDefaultLock(mongoTemplate))
                .addChangeLogsScanPackage("com.github.cloudyrock.mongock.changelogs.client")
                .setSpringContext(springContext)
                .setEventPublisher(eventPublisher)
                .setTrackIgnored(true)
                .buildApplicationRunner();
    }


    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(DateToZonedDateTimeConverter.INSTANCE);
        converters.add(ZonedDateTimeToDateConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }
}
