package com.github.cloudyrock.mongock.changelogs.client;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.client.Client;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;

import static com.github.cloudyrock.mongock.Spring5SpringData3MultipleTemplatesApp.CLIENTS_COLLECTION_NAME;

@ChangeLog(order = "3")
public class ClientUpdaterChangeLog2 {

    @ChangeSet(id = "data-updater-with-mongockTemplate-2", order = "001", author = "mongock")
    public void dataUpdater(MongockTemplate template) {

        template.findAll(Client.class, CLIENTS_COLLECTION_NAME)
                .stream()
                .map(client -> client.setName(client.getName() + "_updated_2"))
                .forEach(client -> template.save(client, CLIENTS_COLLECTION_NAME));

    }


}
