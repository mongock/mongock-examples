package io.mongock.examples.mongodb.springboot.advance.application;

import io.mongock.examples.mongodb.springboot.advance.config.ApplicationConfiguration;
import io.mongock.examples.mongodb.springboot.advance.config.MongoDbCommonConfiguration;
import io.mongock.examples.mongodb.springboot.advance.config.MongockConfiguration;
import io.mongock.api.annotations.MongockCliConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        ApplicationConfiguration.class, // not loaded in the CLI
        MongoDbCommonConfiguration.class,
        MongockConfiguration.class})
@MongockCliConfiguration(sources = {MongoDbCommonConfiguration.class, MongockConfiguration.class})
public class SpringBootAdvanceApp {

    
    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";
    public final static String PRODUCTS_COLLECTION_NAME = "productCollection";

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SpringBootAdvanceApp.class).run(args);
    }

}
