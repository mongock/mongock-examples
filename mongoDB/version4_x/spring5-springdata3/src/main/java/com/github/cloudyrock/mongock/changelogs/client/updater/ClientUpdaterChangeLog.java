package com.github.cloudyrock.mongock.changelogs.client.updater;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.client.Client;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;

import java.util.List;

import static com.github.cloudyrock.mongock.Spring5SpringData3App.CLIENTS_COLLECTION_NAME;

@ChangeLog(order = "2")
public class ClientUpdaterChangeLog {

    @ChangeSet(id = "data-updater-with-mongockTemplate", order = "001", author = "mongock")
    public void dataUpdater(MongockTemplate template) {
        List<Client> clients = template.findAll(Client.class, CLIENTS_COLLECTION_NAME);
        clients.stream()
                .map(client -> client.setName(client.getName() + "_updated"))
                .forEach(client -> template.save(client, CLIENTS_COLLECTION_NAME));

    }


}
