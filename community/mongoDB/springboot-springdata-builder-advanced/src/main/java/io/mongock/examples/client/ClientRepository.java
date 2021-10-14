package io.mongock.examples.client;

import io.mongock.examples.SpringBootSpringDataBuilderAdvancedApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(SpringBootSpringDataBuilderAdvancedApp.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends MongoRepository<Client, String> {

}
