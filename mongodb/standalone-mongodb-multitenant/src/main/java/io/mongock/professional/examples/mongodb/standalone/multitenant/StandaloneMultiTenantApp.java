package io.mongock.professional.examples.mongodb.standalone.multitenant;

import io.mongock.api.annotations.MongockCliConfiguration;

@MongockCliConfiguration(sources = RunnerBuilderProviderImpl.class)
public class StandaloneMultiTenantApp {

  public final static String CLIENTS_COLLECTION_NAME = "clientCollection";
  public final static String PRODUCTS_COLLECTION_NAME = "productCollection";

  public static void main(String[] args) {
    new RunnerBuilderProviderImpl()
            .getBuilder()
            .buildRunner()
            .execute();
  }

}
