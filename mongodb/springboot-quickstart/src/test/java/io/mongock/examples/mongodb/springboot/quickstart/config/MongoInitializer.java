package io.mongock.examples.mongodb.springboot.quickstart.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.List;

public class MongoInitializer  implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static MongoDBContainer instance;

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        List<String> addedProperties = Collections.singletonList(
                "spring.data.mongodb.uri=" + getContainer().getReplicaSetUrl()
        );
        TestPropertyValues.of(addedProperties).applyTo(context.getEnvironment());
    }

    private MongoDBContainer getContainer() {
        if (instance == null) {
            instance = new MongoDBContainer(DockerImageName.parse("mongo:5.0.6"))
                    .withReuse(true);
            instance.start();
        }
        return instance;
    }
}

