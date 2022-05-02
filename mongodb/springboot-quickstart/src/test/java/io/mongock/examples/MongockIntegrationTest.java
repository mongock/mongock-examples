package io.mongock.examples;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.exception.MongockException;
import io.mongock.examples.changeunits.ClientInitializerChangeUnit;
import io.mongock.examples.client.ClientRepository;
import io.mongock.examples.config.MongoInitializer;
import io.mongock.test.springboot.junit5.MongockSpringbootJUnit5IntegrationTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.test.context.ContextConfiguration;

import static io.mongock.examples.QuickStartApp.CLIENTS_COLLECTION_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Quick example of integration testing with Mongock migration with Springboot
 */
@ContextConfiguration(initializers = MongoInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MongockIntegrationTest extends MongockSpringbootJUnit5IntegrationTestBase {

    @Autowired
    private ClientRepository clientRepository;


    private MongoDatabase mongoDatabase;

    @Autowired
    private void initDatabase(MongoDatabaseFactory mongoDatabaseFactory) {
        mongoDatabase = mongoDatabaseFactory.getMongoDatabase();
    }

    @AfterEach
    void afterEach() {
        mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME).drop();
    }

    @Test
    void shouldSucceedFullyWhenCollectionIsNotCreatedYet() {
        executeMongock();
        assertEquals(ClientInitializerChangeUnit.INITIAL_CLIENTS, clientRepository.findAll().size());
    }
    
    @Test
    void shouldFailWhenCollectionIsAlreadyCreated() {
        mongoDatabase.createCollection(CLIENTS_COLLECTION_NAME);
        MongockException ex = Assertions.assertThrows(MongockException.class, this::executeMongock);
        Assertions.assertTrue(ex.getMessage().contains("Collection already exists"));

    }

}