package com.github.cloudyrock.mongock.examples.client;

import com.github.cloudyrock.mongock.examples.SpringBootSpringDataAnnotationAdvancedApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(SpringBootSpringDataAnnotationAdvancedApp.CLIENTS_COLLECTION_NAME)
public interface ClientRepository extends MongoRepository<Client, String> {

}
