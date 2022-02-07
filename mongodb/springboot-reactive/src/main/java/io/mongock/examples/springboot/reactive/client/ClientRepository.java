package io.mongock.examples.springboot.reactive.client;

import io.mongock.examples.springboot.reactive.SpringbootReactiveApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository(SpringbootReactiveApp.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends ReactiveMongoRepository<Client, String> {

}
