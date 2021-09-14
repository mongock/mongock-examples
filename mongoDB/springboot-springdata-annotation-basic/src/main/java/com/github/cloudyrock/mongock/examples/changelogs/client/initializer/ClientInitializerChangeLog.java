package com.github.cloudyrock.mongock.examples.changelogs.client.initializer;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.examples.client.Client;
import com.github.cloudyrock.mongock.examples.client.ClientRepository;
import com.mongodb.client.MongoDatabase;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.cloudyrock.mongock.examples.SpringBootSpringDataAnnotationBasicApp.CLIENTS_COLLECTION_NAME;
import static com.mongodb.client.model.Filters.in;

public class ClientInitializerChangeLog implements io.mongock.api.ChangeLog {

  public static final String ID = "client-initializer";

  public final static int INITIAL_CLIENTS = 10;

  private final ClientRepository clientRepository;

  public ClientInitializerChangeLog(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  @Override
  public String geId() {
    return ID;
  }

  @Override
  public String getOrder() {
    return "1";
  }

  @Override
  public String getAuthor() {
    return "mongock";
  }

  @Override
  public void before() {

  }

  @Override
  public void rollbackBefore() {
  }

  @Override
  public void changeSet() {
    clientRepository.saveAll(
            IntStream.range(0, INITIAL_CLIENTS)
                    .mapToObj(ClientInitializerChangeLog::getClient)
                    .collect(Collectors.toList())
    );
  }

  @Override
  public void rollback() {
    clientRepository.deleteAll();
  }

  private static Client getClient(int i) {
    return new Client("name-" + i, "email-" + i, "phone" + i, "country" + i);
  }
}
