package io.mongock.professional.examples.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.mongock.professional.examples.converters.DateToZonedDateTimeConverter;
import io.mongock.professional.examples.converters.ZonedDateTimeToDateConverter;
import io.mongock.professional.runner.common.multitenant.selector.TenantManager;
import io.mongock.professional.runner.common.multitenant.selector.TenantManagerDefault;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
public class MultiTenantMongoAppConfig {
    public final static String MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/";


    @Bean
    public TenantManager tenantSelector() {
        return new TenantManagerDefault(
                "tenant-1",
                "tenant-2"
        );
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(MONGODB_CONNECTION_STRING);
    }

    @Bean
    @Primary
    public MongoDatabaseFactory mongoDbFactory(MongoClient mongoClient, TenantManager tenantManager) {
        return new MultiTenantMongoDBFactory(mongoClient, tenantManager);
    }


    @Bean
    @Primary
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoTemplate mongoTemplate) {
        MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
        mongoMapping.setCustomConversions( new MongoCustomConversions(
                Arrays.asList(DateToZonedDateTimeConverter.INSTANCE, ZonedDateTimeToDateConverter.INSTANCE)
        ));
        return mongoMapping;
    }




}
