package io.mongock.examples.mongodb.springboot.advance.client;

import io.mongock.examples.mongodb.springboot.advance.application.SpringBootAdvanceApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(SpringBootAdvanceApp.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends MongoRepository<Client, String> {

}
