package io.mongock.professional.examples.client;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import static io.mongock.professional.examples.SpringBootMultitenantApp.CLIENTS_COLLECTION_NAME;

@Repository(CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends MongoRepository<Client, String> {

}
