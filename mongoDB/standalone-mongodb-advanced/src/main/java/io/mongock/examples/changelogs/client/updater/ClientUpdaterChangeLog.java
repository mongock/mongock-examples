package io.mongock.examples.changelogs.client.updater;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import io.mongock.examples.client.Client;

import static com.github.cloudyrock.mongock.examples.StandaloneMongoDbAdvancedApp.CLIENTS_COLLECTION_NAME;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.stream.StreamSupport;
import org.bson.Document;

@ChangeLog(order = "2")
public class ClientUpdaterChangeLog {

  @ChangeSet(id = "data-updater", order = "001", author = "mongock")
  public void dataUpdater(MongoDatabase mongoDatabase) {
    
    MongoCollection<Client> clientCollection = mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME, Client.class);
    
    StreamSupport.stream(clientCollection.find().spliterator(), false)
            .map(client -> client.setName(client.getName() + "_updated"))
            .forEach(client -> clientCollection.replaceOne(getQueryById(client), client));

  }
  
  private static Document getQueryById(Client client) {
    return new Document("_id", client.getId());
  }
}
