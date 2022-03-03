package io.mongock.examples;

import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class RemoteClientService {

    public List<Document> getClientsFromRemoteServer() {
        return Arrays.asList(
                new Document().append("name", "remote-client-1").append("surname", "Henricks").append("type", "remote"),
                new Document().append("name", "remote-client-2").append("surname", "Wilson").append("type", "remote")
        );
    }
}
