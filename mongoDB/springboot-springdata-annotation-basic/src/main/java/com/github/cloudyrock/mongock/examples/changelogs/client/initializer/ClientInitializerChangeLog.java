package com.github.cloudyrock.mongock.examples.changelogs.client.initializer;

import com.github.cloudyrock.mongock.examples.client.Client;
import com.github.cloudyrock.mongock.examples.client.ClientRepository;
import io.mongock.api.ChangeLogInfo;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ChangeLogInfo(id="client-initializer", order = "1", author = "mongock_test")
public class ClientInitializerChangeLog implements io.mongock.api.ChangeLog {

  public static final String ID = "client-initializer";

  public final static int INITIAL_CLIENTS = 10;

  private final ClientRepository clientRepository;

  public ClientInitializerChangeLog(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
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
