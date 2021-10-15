package io.mongock.examples.client;

import io.mongock.examples.SpringBootSpringDataAnnotationBasicApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(SpringBootSpringDataAnnotationBasicApp.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends MongoRepository<Client, String> {

}
