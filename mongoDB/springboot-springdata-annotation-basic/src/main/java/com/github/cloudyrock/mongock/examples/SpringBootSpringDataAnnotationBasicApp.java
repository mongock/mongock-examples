package com.github.cloudyrock.mongock.examples;

import com.github.cloudyrock.mongock.examples.client.ClientRepository;
import com.github.cloudyrock.mongock.examples.spring.DateToZonedDateTimeConverter;
import com.github.cloudyrock.mongock.examples.spring.ZonedDateTimeToDateConverter;
import com.github.cloudyrock.springboot.EnableMongock;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

/**
 * Using @EnableMongock with minimal configuration only requires changeLog package to scan
 * in property file
 */
@EnableMongock
@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ClientRepository.class)
public class SpringBootSpringDataAnnotationBasicApp {

    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

    public static void main(String[] args) {
        getSpringAppBuilder().run(args);
    }

    public static SpringApplicationBuilder getSpringAppBuilder() {
        return new SpringApplicationBuilder().sources(SpringBootSpringDataAnnotationBasicApp.class);
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(DateToZonedDateTimeConverter.INSTANCE);
        converters.add(ZonedDateTimeToDateConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }
}
