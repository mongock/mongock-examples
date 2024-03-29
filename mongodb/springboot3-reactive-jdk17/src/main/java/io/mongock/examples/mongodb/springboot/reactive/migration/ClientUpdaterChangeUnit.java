package io.mongock.examples.mongodb.springboot.reactive.migration;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.ClientSession;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync;
import io.mongock.driver.mongodb.reactive.util.SubscriberSync;
import io.mongock.examples.mongodb.springboot.reactive.SpringbootReactiveApp;
import io.mongock.examples.mongodb.springboot.reactive.client.Client;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;


@ChangeUnit(id = "client-updater", order = "2", author = "mongock")
public class ClientUpdaterChangeUnit {
  private static Logger logger = LoggerFactory.getLogger(ClientUpdaterChangeUnit.class);

  @Execution
  public void execution(ClientSession clientSession, MongoDatabase mongoDatabase) {

    SubscriberSync<Client> subscriber = new MongoSubscriberSync<>();
    MongoCollection<Client> clientCollection = mongoDatabase.getCollection(SpringbootReactiveApp.CLIENTS_COLLECTION_NAME, Client.class);
    clientCollection.find().subscribe(subscriber);

    Function<Client, UpdateResult> clientUpdater = getClientUpdater(clientSession, clientCollection);

    subscriber.get()
            .stream()
            .map(client -> client.setName(client.getName() + "_updated"))
            .map(clientUpdater)
            .forEach(result -> logger.info("result[upsertId:{}, matches: {}, modifies: {}, acknowledged: {}]", result.getUpsertedId(), result.getMatchedCount(), result.getModifiedCount(), result.wasAcknowledged()));
  }

  @RollbackExecution
  public void rollbackExecution(ClientSession clientSession, MongoDatabase mongoDatabase) {
    SubscriberSync<Client> subscriber = new MongoSubscriberSync<>();
    MongoCollection<Client> clientCollection = mongoDatabase.getCollection(SpringbootReactiveApp.CLIENTS_COLLECTION_NAME, Client.class);
    clientCollection.find().subscribe(subscriber);

    Function<Client, UpdateResult> clientUpdater = getClientUpdater(clientSession, clientCollection);

    subscriber.get()
            .stream()
            .map(client -> client.setName(client.getName().substring(0, client.getName().length() - "_updated".length())))
            .map(clientUpdater)
            .forEach(result -> logger.info("result[upsertId:{}, matches: {}, modifies: {}, acknowledged: {}]", result.getUpsertedId(), result.getMatchedCount(), result.getModifiedCount(), result.wasAcknowledged()));
  }
  
  private static Document getQueryById(Client client) {
    return new Document("_id", new ObjectId(client.getId()));
  }

  private static Function<Client, UpdateResult> getClientUpdater(ClientSession clientSession, MongoCollection<Client> clientCollection) {
    return client -> {
      SubscriberSync<UpdateResult> updateSubscriber = new MongoSubscriberSync<>();
      clientCollection.replaceOne(clientSession, getQueryById(client), client).subscribe(updateSubscriber);
      return updateSubscriber.getFirst();
    };
  }
}
