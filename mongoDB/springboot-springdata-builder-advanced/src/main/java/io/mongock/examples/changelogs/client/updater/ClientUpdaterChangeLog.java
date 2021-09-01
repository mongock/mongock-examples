package io.mongock.examples.changelogs.client.updater;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import io.mongock.examples.client.Client;
import io.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;

import static com.github.cloudyrock.mongock.examples.SpringBootSpringDataAnnotationAdvancedApp.CLIENTS_COLLECTION_NAME;

@ChangeLog(order = "2")
public class ClientUpdaterChangeLog {

  @ChangeSet(id = "data-updater-with-mongockTemplate", order = "001", author = "mongock")
  public void dataUpdater(MongockTemplate template) {

    template.findAll(Client.class, CLIENTS_COLLECTION_NAME)
            .stream()
            .map(client -> client.setName(client.getName() + "_updated"))
            .forEach(client -> template.save(client, CLIENTS_COLLECTION_NAME));

  }
}
