package io.mongock.examples.mongodb.springboot.advance.config;


import io.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import io.mongock.runner.springboot.EnableMongock;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.RunnerSpringbootBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * For Mongock autoconfiguration:
 * - Uncomment line 19(@EnableMongock)
 * - Comment the beans for mongock runner and builder. In other words leave the MongockConfiguration class empty.
 */
@EnableMongock
@Configuration
public class MongockConfiguration {

    //	@Bean
//	public MongockInitializingBeanRunner initializingBeanRunner(RunnerSpringbootBuilder runnerBuilder) {
//		// Runner
//		return runnerBuilder.buildInitializingBeanRunner();
//	}
//
//	/**
//	 * The Mongock CLI requires the RunnerBuilder bean
//	 */
    @Bean
    public RunnerSpringbootBuilder runnerBuilder(ApplicationContext springContext,
                                                 ApplicationEventPublisher eventPublisher,
                                                 MongoTemplate mongoTemplate) {
        return MongockSpringboot.builder()
                .setDriver(SpringDataMongoV3Driver.withDefaultLock(mongoTemplate))
                .addMigrationScanPackage("io.mongock.examples.mongodb.springboot.advance.migration")
                .setSpringContext(springContext)
                .setEventPublisher(eventPublisher)
                .setTrackIgnored(true)
                .setTransactional(true);
    }
}
