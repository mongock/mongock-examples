package io.mongock.examples.professional.changelogs.client.initializer;

import io.mongock.examples.professional.client.Client;
import io.mongock.examples.professional.client.ClientRepository;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ChangeUnit(id="client-initializer", order = "1", author = "mongock_test")
public class ClientInitializerChangeLog {

  public static final String ID = "client-initializer";

  public final static int INITIAL_CLIENTS = 10;

  private final ClientRepository clientRepository;

  public ClientInitializerChangeLog(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }



  @BeforeExecution
  public void before() {

  }

  @RollbackBeforeExecution
  public void rollbackBefore() {
  }

  @Execution
  public void changeSet() {
    clientRepository.saveAll(
            IntStream.range(0, INITIAL_CLIENTS)
                    .mapToObj(ClientInitializerChangeLog::getClient)
                    .collect(Collectors.toList())
    );
  }

  @RollbackExecution
  public void rollback() {
    clientRepository.deleteAll();
  }

  private static Client getClient(int i) {
    return new Client("name-" + i, "email-" + i, "phone" + i, "country" + i);
  }
}
