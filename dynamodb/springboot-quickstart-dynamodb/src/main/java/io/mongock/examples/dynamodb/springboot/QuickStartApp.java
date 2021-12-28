package io.mongock.examples.dynamodb.springboot;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;


/**
 * Using @EnableMongock with minimal configuration only requires changeLog package to scan
 * in property file
 */
@EnableMongock
@SpringBootApplication
public class QuickStartApp {
    private static final String SERVICE_ENDPOINT = System.getenv("AWS_SERVICE_ENDPOINT") != null
            ? System.getenv("AWS_SERVICE_ENDPOINT")
            : "dynamodb.eu-west-1.amazonaws.com";
    private static final String REGION = System.getenv("AWS_REGION") != null
            ? System.getenv("AWS_REGION")
            : "eu-west-1";
    private static final String ACCESS_KEY = System.getenv("AWS_ACCESS_KEY");
    private static final String SECRET_KEY = System.getenv("AWS_SECRET_KEY");

    public static void main(String[] args) {
        getSpringAppBuilder().run(args);
    }

    public static SpringApplicationBuilder getSpringAppBuilder() {
        return new SpringApplicationBuilder().sources(QuickStartApp.class);
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
    @Bean
    public AmazonDynamoDBClient amazonDynamoDBClient() {
        return (AmazonDynamoDBClient) AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(SERVICE_ENDPOINT, REGION))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
                .build();
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDBClient client) {
        DynamoDBMapperConfig config = DynamoDBMapperConfig
                .builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
                .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING)
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(Client.TABLE_NAME))
                .build();
        return new DynamoDBMapper(client, config);
    }

    @Bean
    public DynamoDB dynamoDB(AmazonDynamoDBClient client) {
        return new DynamoDB(client);
    }

}
