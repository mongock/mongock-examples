package io.mongock.professional.examples.mongodb.springboot.multitenant.client;

import io.mongock.professional.examples.mongodb.springboot.multitenant.SpringBootMultitenantApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(SpringBootMultitenantApp.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends MongoRepository<Client, String> {

}
