package io.mongock.examples.mongodb.springboot3.springdata4.client;

import io.mongock.examples.mongodb.springboot3.springdata4.App;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(App.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends MongoRepository<Client, String> {

}
