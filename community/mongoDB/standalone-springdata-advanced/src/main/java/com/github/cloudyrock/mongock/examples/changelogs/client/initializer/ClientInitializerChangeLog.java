package com.github.cloudyrock.mongock.examples.changelogs.client.initializer;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.github.cloudyrock.mongock.examples.client.Client;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ChangeLog(order = "1")
public class ClientInitializerChangeLog {

  public final static int INITIAL_CLIENTS = 10;

  @ChangeSet(id = "data-initializer-with-mongockTemplate", order = "001", author = "mongock")
  public void dataInitializer(MongockTemplate mongockTemplate) {
    
    mongockTemplate.insertAll(
            IntStream.range(0, INITIAL_CLIENTS)
                    .mapToObj(ClientInitializerChangeLog::getClient)
                    .collect(Collectors.toList())
    );
  }

  private static Client getClient(int i) {
    return new Client("name-" + i, "email-" + i, "phone" + i, "country" + i);
  }
}
