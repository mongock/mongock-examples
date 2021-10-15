package io.mongock.examples;

import io.mongock.api.annotations.MongockCliConfiguration;
import io.mongock.examples.client.ClientRepository;
import io.mongock.examples.config.MongoDbCommonConfiguration;
import io.mongock.examples.config.MongockAutoConfiguration;
import io.mongock.examples.config.MongockBuilderConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//This is only added to deactivate the Mongock builder configuration class. It shouldn't be needed in a real scenario
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {MongockBuilderConfiguration.class, SpringBootBuilderApp.class}))
@MongockCliConfiguration(sources = {MongoDbCommonConfiguration.class, MongockAutoConfiguration.class})
public class SpringBootAutoconfigurationApp {

    
    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";
    public final static String PRODUCTS_COLLECTION_NAME = "productCollection";

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SpringBootAutoconfigurationApp.class).run(args);
    }

}
