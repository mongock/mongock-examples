package com.github.cloudyrock.mongock.examples.client;

import com.github.cloudyrock.mongock.examples.SpringBootSpringDataBuilderBasicApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(SpringBootSpringDataBuilderBasicApp.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends MongoRepository<Client, String> {

}
