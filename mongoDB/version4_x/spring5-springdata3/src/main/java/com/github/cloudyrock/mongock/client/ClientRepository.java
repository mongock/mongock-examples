package com.github.cloudyrock.mongock.client;

import com.github.cloudyrock.mongock.Mongock4Spring5SpringData3App;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(Mongock4Spring5SpringData3App.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends MongoRepository<Client, String> {

}
