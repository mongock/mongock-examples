package io.mongock.professional.examples;

import io.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import io.mongock.professional.examples.client.ClientRepository;
import io.mongock.professional.runner.common.multitenant.selector.TenantManager;
import io.mongock.professional.runner.common.multitenant.wrapper.MultiTenant;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.RunnerSpringbootBuilder;
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import io.mongock.utils.Constants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ClientRepository.class)
public class SpringBootMultitenantApp {

    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";



    public static void main(String[] args) {
        getSpringAppBuilder().run(args);
    }

    private static SpringApplicationBuilder getSpringAppBuilder() {
        return new SpringApplicationBuilder().sources(SpringBootMultitenantApp.class);
    }

    @Bean
    public RunnerSpringbootBuilder runnerSpringbootBuilderProfessional(MongoTemplate mongoTemplate,
                                                                       ApplicationContext springContext,
                                                                       TenantManager tenantManager) {
        ;

        return MongockSpringboot.builder()
                .setDriverMultiTenant(buildDriverTemplate(mongoTemplate), tenantManager)
                .addMigrationScanPackage("io.mongock.professional.examples.changelogs")
                .setSpringContext(springContext)
                .setTransactionEnabled(true);
    }

    @Bean
    @Profile(Constants.NON_CLI_PROFILE)
    public MongockInitializingBeanRunner mongockRunner(RunnerSpringbootBuilder builder) {
        return builder.buildInitializingBeanRunner();
    }

    private SpringDataMongoV3Driver buildDriverTemplate(MongoTemplate mongoTemplate) {
        SpringDataMongoV3Driver driver = SpringDataMongoV3Driver.withDefaultLock(mongoTemplate);
        driver.enableTransaction();
        return driver;
    }


}
