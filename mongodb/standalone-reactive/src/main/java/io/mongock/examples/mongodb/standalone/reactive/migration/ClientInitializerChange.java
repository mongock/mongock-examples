package io.mongock.examples.mongodb.standalone.reactive.migration;


import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.reactivestreams.client.ClientSession;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync;
import io.mongock.driver.mongodb.reactive.util.SubscriberSync;
import io.mongock.examples.mongodb.standalone.reactive.client.Client;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.mongock.examples.mongodb.standalone.reactive.StandaloneReactiveApp.CLIENTS_COLLECTION_NAME;


@ChangeUnit(id = "client-initializer", order = "1", author = "mongock")
public class ClientInitializerChange {

    public final static int INITIAL_CLIENTS = 10;
    private static Logger logger = LoggerFactory.getLogger(ClientInitializerChange.class);

    @BeforeExecution
    public void beforeExecution(MongoDatabase mongoDatabase) {
        SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.createCollection(CLIENTS_COLLECTION_NAME).subscribe(subscriber);
        subscriber.await();
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoDatabase mongoDatabase) {
        SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME).drop().subscribe(subscriber);
        subscriber.await();
    }

    @Execution
    public void execution(ClientSession clientSession, MongoDatabase mongoDatabase) {
        SubscriberSync<InsertManyResult> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection(CLIENTS_COLLECTION_NAME, Client.class)
                .insertMany(clientSession, IntStream.range(0, INITIAL_CLIENTS)
                        .mapToObj(ClientInitializerChange::getClient)
                        .collect(Collectors.toList()))
                .subscribe(subscriber);
        InsertManyResult result = subscriber.getFirst();
        logger.info("ClientInitializerChangeLog.execution wasAcknowledged: {}", result.wasAcknowledged());
        result.getInsertedIds()
                .entrySet()
                .forEach(entry -> logger.info("update id[{}] : {}", entry.getKey(), entry.getValue()));
    }

    @RollbackExecution
    public void rollbackExecution(ClientSession clientSession, MongoDatabase mongoDatabase) {
        SubscriberSync<DeleteResult> subscriber = new MongoSubscriberSync<>();

        mongoDatabase
                .getCollection(CLIENTS_COLLECTION_NAME, Client.class)
                .deleteMany(clientSession, new Document())
                .subscribe(subscriber);
        DeleteResult result = subscriber.getFirst();
        logger.info("ClientInitializerChangeLog.rollbackExecution wasAcknowledged: {}", result.wasAcknowledged());
        logger.info("ClientInitializerChangeLog.rollbackExecution deleted count: {}", result.getDeletedCount());
    }

    private static Client getClient(int i) {
        return new Client("name-" + i, "email-" + i, "phone" + i, "country" + i);
    }
}
