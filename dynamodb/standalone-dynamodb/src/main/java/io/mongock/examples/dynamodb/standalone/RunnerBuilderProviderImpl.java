package io.mongock.examples.dynamodb.standalone;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import io.mongock.driver.dynamodb.driver.DynamoDBDriver;
import io.mongock.examples.dynamodb.standalone.events.MongockEventListener;
import io.mongock.runner.core.builder.RunnerBuilder;
import io.mongock.runner.core.builder.RunnerBuilderProvider;
import io.mongock.runner.standalone.MongockStandalone;


public class RunnerBuilderProviderImpl implements RunnerBuilderProvider {

    private static final DynamoDBMapperConfig config = DynamoDBMapperConfig
            .builder()
            .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
            .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING)
            .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(Client.TABLE_NAME))
			.build();

    @Override
    public RunnerBuilder getBuilder() {
        AmazonDynamoDBClient client = getMainDynamoDBClient();
        return MongockStandalone.builder()
                .setDriver(DynamoDBDriver.withDefaultLock(client))
                .addMigrationScanPackage("io.mongock.examples.dynamodb.standalone.migration")
                .setMigrationStartedListener(MongockEventListener::onStart)
                .setMigrationSuccessListener(MongockEventListener::onSuccess)
                .setMigrationFailureListener(MongockEventListener::onFail)
                .addDependency(new DynamoDBMapper(client, config))
                .addDependency(new DynamoDB(client))
                .addDependency(client);
    }

    /**
     * Main AmazonDynamoDBClient for Mongock to work.
     * <p>
     * AWS_ACCESS_KEY: It's retrieved from ENV variable(mandatory)
     * AWS_SECRET_KEY: It's retrieved from ENV variable(mandatory)
     * AWS_SERVICE_ENDPOINT: It's retrieved from ENV variable(default value: dynamodb.eu-west-1.amazonaws.com)
     * AWS_REGION: It's retrieved from ENV variable(default value: eu-west-1)
     */
    private static AmazonDynamoDBClient getMainDynamoDBClient() {
        return (AmazonDynamoDBClient) AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        "http://localhost:8000",
                        Regions.US_EAST_1.getName()))
                .build();
    }

}
