package io.mongock.examples.test;

import com.github.silaev.mongodb.replicaset.MongoDbReplicaSet;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongocDBContextWithTransaction {


    public final static String MONGODB_MAIN_DB_NAME = "test";

    private static final String MONGO_IMAGE = "mongo:4.2.6";


    @Bean
    public MongoDbReplicaSet mongoDBContainer() {
        MongoDbReplicaSet mongoContainer =  MongoDbReplicaSet
                .builder()
                .mongoDockerImageName(MONGO_IMAGE)
                .build();
        mongoContainer.start();
        return mongoContainer;
    }

    @Bean
    public MongoClient mongoClient(MongoDbReplicaSet mongoDBContainer) {
        return MongoClients.create(mongoDBContainer.getReplicaSetUrl());
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, MONGODB_MAIN_DB_NAME);
    }


}
