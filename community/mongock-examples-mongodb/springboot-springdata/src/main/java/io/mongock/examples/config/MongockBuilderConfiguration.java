package io.mongock.examples.config;


import io.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.RunnerSpringbootBuilder;
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongockBuilderConfiguration {

	@Bean
	public MongockInitializingBeanRunner initializingBeanRunner2(RunnerSpringbootBuilder runnerBuilder) {
		// Runner
		return runnerBuilder.buildInitializingBeanRunner();
	}

	/**
	 * The Mongock CLI requires the RunnerBuilder bean
	 */
	@Bean
	public RunnerSpringbootBuilder runnerBuilder(ApplicationContext springContext,
												 ApplicationEventPublisher eventPublisher,
												 MongoTemplate mongoTemplate,
												 MongoTransactionManager mongoTransactionManager) {
		// Driver
		SpringDataMongoV3Driver driver = SpringDataMongoV3Driver.withDefaultLock(mongoTemplate);
		driver.enableTransactionWithTxManager(mongoTransactionManager);

		return MongockSpringboot.builder()
				.setDriver(driver)
				.addMigrationScanPackage("io.mongock.examples.changelogs")
				.setSpringContext(springContext)
				.setEventPublisher(eventPublisher)
				.setTrackIgnored(true)
				.setTransactionEnabled(true);
	}
}
