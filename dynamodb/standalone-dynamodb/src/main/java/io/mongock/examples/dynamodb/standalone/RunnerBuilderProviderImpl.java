package io.mongock.examples.dynamodb.standalone;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
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

	private static final String SERVICE_ENDPOINT = System.getenv("AWS_ACCESS_KEY") != null
			? System.getenv("AWS_SERVICE_ENDPOINT")
			: "dynamodb.eu-west-1.amazonaws.com";
	private static final String REGION = System.getenv("AWS_REGION") != null
			? System.getenv("AWS_REGION")
			: "eu-west-1";
	private static final String ACCESS_KEY = System.getenv("AWS_ACCESS_KEY");
	private static final String SECRET_KEY = System.getenv("AWS_SECRET_KEY");

	private static final DynamoDBMapperConfig config = DynamoDBMapperConfig
			.builder()
			.withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
			.withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING)
			.withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(Client.TABLE_NAME))
			.build();

	@Override
	public RunnerBuilder getBuilder() {
		AmazonDynamoDBClient client = getMainDynamoDBClient();
		DynamoDB dynamoDB = new DynamoDB(client);
		DynamoDBMapper mapper = new DynamoDBMapper(client, config);
		return MongockStandalone.builder()
				.setDriver(DynamoDBDriver.withDefaultLock(client))
				.addMigrationScanPackage("io.mongock.examples.dynamodb.standalone.migration")
				.setMigrationStartedListener(MongockEventListener::onStart)
				.setMigrationSuccessListener(MongockEventListener::onSuccess)
				.setMigrationFailureListener(MongockEventListener::onFail)
				.addDependency(mapper)
				.addDependency(dynamoDB)
				.addDependency(client);
	}

	/**
	 * Main AmazonDynamoDBClient for Mongock to work.
	 *
	 * AWS_ACCESS_KEY: It's retrieved from ENV variable(mandatory)
	 * AWS_SECRET_KEY: It's retrieved from ENV variable(mandatory)
	 * AWS_SERVICE_ENDPOINT: It's retrieved from ENV variable(default value: dynamodb.eu-west-1.amazonaws.com)
	 * AWS_REGION: It's retrieved from ENV variable(default value: eu-west-1)
	 *
	 */
	private static AmazonDynamoDBClient getMainDynamoDBClient() {
		return (AmazonDynamoDBClient) AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(SERVICE_ENDPOINT, REGION))
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
				.build();
	}

}
