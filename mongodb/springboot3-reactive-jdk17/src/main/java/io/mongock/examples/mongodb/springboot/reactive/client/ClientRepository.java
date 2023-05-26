package io.mongock.examples.mongodb.springboot.reactive.client;

import io.mongock.examples.mongodb.springboot.reactive.SpringbootReactiveApp;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository(SpringbootReactiveApp.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends ReactiveMongoRepository<Client, String> {

}
