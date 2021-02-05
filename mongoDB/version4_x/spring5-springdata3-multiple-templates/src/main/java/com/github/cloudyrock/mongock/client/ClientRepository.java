package com.github.cloudyrock.mongock.client;

import com.github.cloudyrock.mongock.Spring5SpringData3MultipleTemplatesApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(Spring5SpringData3MultipleTemplatesApp.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends MongoRepository<Client, String> {

}
