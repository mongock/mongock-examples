package com.github.cloudyrock.mongock.client;

import com.github.cloudyrock.mongock.Spring5SpringDataEventsApp3;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(Spring5SpringDataEventsApp3.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends MongoRepository<Client, String> {

}
