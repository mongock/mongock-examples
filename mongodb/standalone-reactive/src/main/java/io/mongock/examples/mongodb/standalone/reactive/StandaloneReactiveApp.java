package io.mongock.examples.mongodb.standalone.reactive;

import io.mongock.api.annotations.MongockCliConfiguration;

@MongockCliConfiguration(sources = RunnerBuilderProviderImpl.class)
public class StandaloneReactiveApp {

    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

    public static void main(String[] args) {
        new RunnerBuilderProviderImpl()
                .getBuilder()
                .buildRunner()
                .execute();
    }
}
