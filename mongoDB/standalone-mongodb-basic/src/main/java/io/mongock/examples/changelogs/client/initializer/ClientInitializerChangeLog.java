package io.mongock.examples.changelogs.client.initializer;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import io.mongock.examples.client.Client;
import com.mongodb.client.MongoDatabase;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.cloudyrock.mongock.examples.StandaloneMongoDbBasicApp.CLIENTS_COLLECTION_NAME;

@ChangeLog(order = "1")
public class ClientInitializerChangeLog {

  public final static int INITIAL_CLIENTS = 10;

  @ChangeSet(id = "data-initializer", order = "001", author = "mongock")
  public void dataInitializer(MongoDatabase mongoDatabase) {
    
    mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME, Client.class)
            .insertMany(IntStream.range(0, INITIAL_CLIENTS)
                    .mapToObj(ClientInitializerChangeLog::getClient)
                    .collect(Collectors.toList()));
  }

  private static Client getClient(int i) {
    return new Client("name-" + i, "email-" + i, "phone" + i, "country" + i);
  }
}
