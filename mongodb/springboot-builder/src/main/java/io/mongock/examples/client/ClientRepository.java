package io.mongock.examples.client;

import io.mongock.examples.BuilderApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(BuilderApp.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends MongoRepository<Client, String> {

}
