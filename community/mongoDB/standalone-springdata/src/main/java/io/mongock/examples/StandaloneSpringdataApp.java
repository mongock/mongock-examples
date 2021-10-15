package io.mongock.examples;

public class StandaloneSpringdataApp {


  public final static String CLIENTS_COLLECTION_NAME = "clientCollection";
  public final static String PRODUCTS_COLLECTION_NAME = "productCollection";

  public static void main(String[] args) {
    new RunnerBuilderProviderImpl()
            .getBuilder()
            .buildRunner()
            .execute();
  }

}
