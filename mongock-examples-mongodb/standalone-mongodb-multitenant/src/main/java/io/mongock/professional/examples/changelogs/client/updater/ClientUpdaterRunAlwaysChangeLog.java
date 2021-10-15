package io.mongock.professional.examples.changelogs.client.updater;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import io.mongock.professional.examples.client.Client;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.stream.StreamSupport;
import org.bson.Document;

import static io.mongock.professional.examples.StandaloneMongoDbMultitenantApp.CLIENTS_COLLECTION_NAME;

@ChangeUnit(id="client-updater-runalways", order = "3", author = "mongock", runAlways = true)
public class ClientUpdaterRunAlwaysChangeLog {
  
  @Execution
  public void execution(MongoDatabase mongoDatabase) {

    MongoCollection<Client> clientCollection = mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME, Client.class);
    
    StreamSupport.stream(clientCollection.find().spliterator(), false)
            .map(client -> client.setCounter(client.getCounter() + 1))
            .forEach(client -> clientCollection.replaceOne(getQueryById(client), client));
  }

  @RollbackExecution
  public void rollbackExecution(MongoDatabase mongoDatabase) {

    MongoCollection<Client> clientCollection = mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME, Client.class);
    
    StreamSupport.stream(clientCollection.find().spliterator(), false)
            .map(client -> client.setCounter(client.getCounter() - 1))
            .forEach(client -> clientCollection.replaceOne(getQueryById(client), client));
  }

  private static Document getQueryById(Client client) {
    return new Document("_id", client.getId());
  }
}
