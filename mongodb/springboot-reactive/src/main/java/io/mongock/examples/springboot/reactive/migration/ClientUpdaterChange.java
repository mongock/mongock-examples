package io.mongock.examples.springboot.reactive.migration;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.ClientSession;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync;
import io.mongock.driver.mongodb.reactive.util.SubscriberSync;
import io.mongock.examples.springboot.reactive.client.Client;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

import static io.mongock.examples.springboot.reactive.SpringbootReactiveApp.CLIENTS_COLLECTION_NAME;


@ChangeUnit(id = "client-updater", order = "2", author = "mongock")
public class ClientUpdaterChange {
  private static Logger logger = LoggerFactory.getLogger(ClientUpdaterChange.class);

  @Execution
  public void execution(ClientSession clientSession, MongoDatabase mongoDatabase) {

    SubscriberSync<Document> subscriber = new MongoSubscriberSync<>();
    MongoCollection<Document> clientCollection = mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME);
    clientCollection.find().subscribe(subscriber);

    Function<Document, UpdateResult> clientUpdater = getClientUpdater(clientSession, clientCollection);

    subscriber.get()
            .stream()
            .peek(clientDoc -> clientDoc.put("name", clientDoc.getString("name") + "_updated"))
            .map(clientUpdater)
            .forEach(result -> logger.info("result[upsertId:{}, matches: {}, modifies: {}, acknowledged: {}]", result.getUpsertedId(), result.getMatchedCount(), result.getModifiedCount(), result.wasAcknowledged()));
  }

  @RollbackExecution
  public void rollbackExecution(ClientSession clientSession, MongoDatabase mongoDatabase) {
    SubscriberSync<Document> subscriber = new MongoSubscriberSync<>();
    MongoCollection<Document> clientCollection = mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME);
    clientCollection.find().subscribe(subscriber);

    Function<Document, UpdateResult> clientUpdater = getClientUpdater(clientSession, clientCollection);

    subscriber.get()
            .stream()
            .peek(clientDoc -> clientDoc.put("name", clientDoc.getString("name").substring(0, clientDoc.getString("name").length() - "_updated".length())))
            .map(clientUpdater)
            .forEach(result -> logger.info("result[upsertId:{}, matches: {}, modifies: {}, acknowledged: {}]", result.getUpsertedId(), result.getMatchedCount(), result.getModifiedCount(), result.wasAcknowledged()));
  }
  
  private static Document getQueryById(Document client) {
    return new Document("_id", client.getObjectId("_id").toString());
  }

  private static Function<Document, UpdateResult> getClientUpdater(ClientSession clientSession, MongoCollection<Document> clientCollection) {
    return client -> {
      SubscriberSync<UpdateResult> updateSubscriber = new MongoSubscriberSync<>();
      clientCollection.replaceOne(clientSession, getQueryById(client), client).subscribe(updateSubscriber);
      return updateSubscriber.getFirst();
    };
  }
}
