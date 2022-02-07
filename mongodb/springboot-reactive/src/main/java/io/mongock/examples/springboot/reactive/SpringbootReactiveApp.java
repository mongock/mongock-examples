package io.mongock.examples.springboot.reactive;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import io.mongock.examples.springboot.reactive.client.ClientRepository;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Using @EnableMongock with minimal configuration only requires changeLog package to scan
 * in property file
 */
@SpringBootApplication
@EnableReactiveMongoRepositories(basePackageClasses = ClientRepository.class)
public class SpringbootReactiveApp {
    public final static String MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/";

    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SpringbootReactiveApp.class).run(args);
    }

    @Bean
    public MongockInitializingBeanRunner getBuilder(MongoClient reactiveMongoClient,
                                                    ApplicationContext context) {
        return MongockSpringboot.builder()
                .setDriver(MongoReactiveDriver.withDefaultLock(reactiveMongoClient, "test"))
                .addMigrationScanPackage("io.mongock.examples.springboot.reactive.migration")
                .setSpringContext(context)
                .setTransactionEnabled(true)
                .buildInitializingBeanRunner();
    }

    @Bean
    MongoClient mongoClient() {
        CodecRegistry codecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));




        return MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(MONGODB_CONNECTION_STRING))
                .codecRegistry(codecRegistry)
                .build());
    }

}
