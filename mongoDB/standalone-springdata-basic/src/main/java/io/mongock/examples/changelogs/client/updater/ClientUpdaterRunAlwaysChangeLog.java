package io.mongock.examples.changelogs.client.updater;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import io.mongock.examples.client.Client;
import io.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;

import static com.github.cloudyrock.mongock.examples.StandaloneSpringDataBasicApp.CLIENTS_COLLECTION_NAME;

@ChangeLog(order = "3")
public class ClientUpdaterRunAlwaysChangeLog {

  @ChangeSet(id = "data-updater-runAlways-with-mongockTemplate", order = "001", author = "mongock", runAlways = true)
  public void dataUpdater(MongockTemplate template) {

    template.findAll(Client.class, CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setCounter(client.getCounter() + 1))
            .forEach(client -> template.save(client, CLIENTS_COLLECTION_NAME));

  }
}