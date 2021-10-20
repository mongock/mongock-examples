package io.mongock.examples.changelogs.client.updater;

import com.mongodb.client.ClientSession;
import io.mongock.examples.client.Client;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.stream.StreamSupport;
import org.bson.Document;

import static io.mongock.examples.StandaloneMongoApp.CLIENTS_COLLECTION_NAME;

@ChangeUnit(id = "client-updater", order = "2", author = "mongock")
public class ClientUpdaterChangeLog {

  @Execution
  public void execution(ClientSession clientSession, MongoDatabase mongoDatabase) {
    
    MongoCollection<Client> clientCollection = mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME, Client.class);
    
    StreamSupport.stream(clientCollection.find().spliterator(), false)
            .map(client -> client.setName(client.getName() + "_updated"))
            .forEach(client -> clientCollection.replaceOne(clientSession, getQueryById(client), client));
  }
  
  @RollbackExecution
  public void rollbackExecution(ClientSession clientSession, MongoDatabase mongoDatabase) {
    
    MongoCollection<Client> clientCollection = mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME, Client.class);
    
    StreamSupport.stream(clientCollection.find().spliterator(), false)
            .map(client -> client.setName(client.getName().substring(0, client.getName().length() - "_updated".length())))
            .forEach(client -> clientCollection.replaceOne(clientSession, getQueryById(client), client));
  }
  
  private static Document getQueryById(Client client) {
    return new Document("_id", client.getId());
  }
}
