package io.mongock.professional.examples.mongodb.standalone.multitenant.migration.initializer;

import com.mongodb.client.ClientSession;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

import io.mongock.professional.examples.mongodb.standalone.multitenant.client.Client;
import com.mongodb.client.MongoDatabase;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.mongock.professional.examples.mongodb.standalone.multitenant.StandaloneMultiTenantApp;
import org.bson.Document;

@ChangeUnit(id="client-initializer", order = "1", author = "mongock")
public class ClientInitializerChangeUnit {

  public final static int INITIAL_CLIENTS = 10;
  
  @BeforeExecution
  public void beforeExecution(MongoDatabase mongoDatabase) {
      
      mongoDatabase.createCollection(StandaloneMultiTenantApp.CLIENTS_COLLECTION_NAME);
  }
  
  @RollbackBeforeExecution
  public void rollbackBeforeExecution(MongoDatabase mongoDatabase) {
      
      mongoDatabase.getCollection(StandaloneMultiTenantApp.CLIENTS_COLLECTION_NAME).drop();
  }

  @Execution
  public void execution(ClientSession clientSession, MongoDatabase mongoDatabase) {
    
    mongoDatabase.getCollection(StandaloneMultiTenantApp.CLIENTS_COLLECTION_NAME, Client.class)
            .insertMany(clientSession, IntStream.range(0, INITIAL_CLIENTS)
                    .mapToObj(ClientInitializerChangeUnit::getClient)
                    .collect(Collectors.toList()));
  }
  
  @RollbackExecution
  public void rollbackExecution(ClientSession clientSession, MongoDatabase mongoDatabase) {
    mongoDatabase.getCollection(StandaloneMultiTenantApp.CLIENTS_COLLECTION_NAME, Client.class).deleteMany(clientSession, new Document());
  }

  private static Client getClient(int i) {
    return new Client("name-" + i, "email-" + i, "phone" + i, "country" + i);
  }
}
