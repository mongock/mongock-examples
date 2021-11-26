package io.mongock.examples.dynamodb.standalone;

import io.mongock.api.annotations.MongockCliConfiguration;

@MongockCliConfiguration(sources = RunnerBuilderProviderImpl.class)
public class StandaloneDynamoDBApp {

  public static void main(String[] args) {
    new RunnerBuilderProviderImpl()
            .getBuilder()
            .buildRunner()
            .execute();
  }

}
