package io.mongock.professional.examples;

import com.mongodb.client.MongoClients;
import io.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import io.mongock.professional.examples.spring.DateToZonedDateTimeConverter;
import io.mongock.professional.examples.spring.ZonedDateTimeToDateConverter;
import io.mongock.professional.runner.common.multitenant.wrapper.MultiTenant;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.RunnerSpringbootBuilder;
import io.mongock.runner.springboot.base.MongockApplicationRunner;
import io.mongock.utils.Constants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@SpringBootApplication
public class SpringBootMultitenantApp {

    public final static String MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/";
    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

    /**
     * Custom converters to map ZonedDateTime.
     */
    private static final MongoCustomConversions customConversions = new MongoCustomConversions(
            Arrays.asList(DateToZonedDateTimeConverter.INSTANCE, ZonedDateTimeToDateConverter.INSTANCE)
    );

    public static void main(String[] args) {
        getSpringAppBuilder().run(args);
    }

    private static SpringApplicationBuilder getSpringAppBuilder() {
        return new SpringApplicationBuilder().sources(SpringBootMultitenantApp.class);
    }

    @Bean
    public RunnerSpringbootBuilder runnerSpringbootBuilderProfessional(ApplicationContext springContext) {
        // Runner
//        SpringDataMongoV3DriverProfessional.multiTenant()
//                .addTenant(getDriver("test1"))
//                .addTenant(getDriver("test2"))
//                .addTenant(getDriver("test3"))

        return MongockSpringboot.builder()
                .setMultiTenant(MultiTenant.withDrivers())
                .addMigrationScanPackage("io.mongock.professional.examples.changelogs")
                .setSpringContext(springContext)
                .setTransactionEnabled(true);
    }

    @Bean
    @Profile(Constants.NON_CLI_PROFILE)
    public MongockApplicationRunner mongockRunner(RunnerSpringbootBuilder builder) {
        return builder.buildApplicationRunner();
    }

    private SpringDataMongoV3Driver getDriver(String dbName) {
        MongoTemplate mongoTemplate = this.generateMongoTemplate(dbName);
        SpringDataMongoV3Driver driver = SpringDataMongoV3Driver.withDefaultLock(mongoTemplate);
        driver.enableTransaction();
        return driver;
    }

    private MongoTemplate generateMongoTemplate(String dbName) {
      MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(MONGODB_CONNECTION_STRING), dbName);
      // Custom converters to map ZonedDateTime.
      MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
      mongoMapping.setCustomConversions(customConversions);
      mongoMapping.afterPropertiesSet();
      return mongoTemplate;
    }

}
