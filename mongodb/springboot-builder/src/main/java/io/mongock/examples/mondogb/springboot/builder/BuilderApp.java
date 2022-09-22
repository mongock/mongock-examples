package io.mongock.examples.mondogb.springboot.builder;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import io.mongock.examples.mondogb.springboot.builder.client.ClientRepository;
import io.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ClientRepository.class)
public class BuilderApp {

    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

    public static void main(String[] args) {
        getSpringAppBuilder().run(args);
    }

    public static SpringApplicationBuilder getSpringAppBuilder() {
        return new SpringApplicationBuilder().sources(BuilderApp.class);
    }
    
    @Bean
    public MongockApplicationRunner mongockApplicationRunner(ApplicationContext springContext,
                                                              MongoTemplate mongoTemplate) {
        return MongockSpringboot.builder()
                .setDriver(SpringDataMongoV3Driver.withDefaultLock(mongoTemplate))
                .addMigrationScanPackage("io.mongock.examples.mondogb.springboot.builder.migration")
                .setEventPublisher(springContext)
                .setSpringContext(springContext)
                .setTransactionEnabled(true)
                .buildApplicationRunner();
    }
    
    /**
     * Transaction Manager.
     * Needed to allow execution of changeSets in transaction scope.
     */
    @Bean
    public MongoTransactionManager transactionManager(MongoTemplate mongoTemplate) {
        TransactionOptions transactionalOptions = TransactionOptions.builder()
                .readConcern(ReadConcern.MAJORITY)
                .readPreference(ReadPreference.primary())
                .writeConcern(WriteConcern.MAJORITY.withJournal(true))
                .build();
        return new MongoTransactionManager(mongoTemplate.getMongoDbFactory(), transactionalOptions);
    }
}
