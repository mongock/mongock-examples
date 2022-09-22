package io.mongock.examples.mongodb.springboot.quickstart.client;

import io.mongock.examples.mongodb.springboot.quickstart.QuickStartApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(QuickStartApp.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends MongoRepository<Client, String> {

}
