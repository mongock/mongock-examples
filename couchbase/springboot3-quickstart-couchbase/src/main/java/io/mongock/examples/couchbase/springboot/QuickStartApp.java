package io.mongock.examples.couchbase.springboot;

import io.mongock.driver.api.entry.ChangeEntryService;
import io.mongock.driver.couchbase.driver.CouchbaseDriver;
import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Using @EnableMongock with minimal configuration only requires changeLog package to scan
 * in property file
 */
@EnableMongock
@SpringBootApplication
public class QuickStartApp {

    public static void main(String[] args) {
        SpringApplication.run(QuickStartApp.class, args);
    }

    @Bean
    ChangeEntryService changeEntryService(CouchbaseDriver couchbaseDriver){
        return couchbaseDriver.getChangeEntryService();
    }
    
    
}
