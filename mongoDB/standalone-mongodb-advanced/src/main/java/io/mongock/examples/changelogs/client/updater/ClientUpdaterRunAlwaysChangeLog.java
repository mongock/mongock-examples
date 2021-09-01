package io.mongock.examples.changelogs.client.updater;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import static com.github.cloudyrock.mongock.examples.StandaloneMongoDbAdvancedApp.CLIENTS_COLLECTION_NAME;
import io.mongock.examples.client.Client;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.stream.StreamSupport;
import org.bson.Document;

@ChangeLog(order = "3")
public class ClientUpdaterRunAlwaysChangeLog {

  @ChangeSet(id = "data-updater-runAlways", order = "001", author = "mongock", runAlways = true)
  public void dataUpdater(MongoDatabase mongoDatabase) {

    MongoCollection<Client> clientCollection = mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME, Client.class);
    
    StreamSupport.stream(clientCollection.find().spliterator(), false)
            .map(client -> client.setCounter(client.getCounter() + 1))
            .forEach(client -> clientCollection.replaceOne(getQueryById(client), client));
  }

  private static Document getQueryById(Client client) {
    return new Document("_id", client.getId());
  }
}
