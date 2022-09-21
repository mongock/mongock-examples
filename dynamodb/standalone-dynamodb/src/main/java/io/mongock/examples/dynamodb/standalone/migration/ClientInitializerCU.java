package io.mongock.examples.dynamodb.standalone.migration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.Put;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.TransactWriteItem;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import io.mongock.driver.dynamodb.repository.DynamoDBTransactionItems;
import io.mongock.examples.dynamodb.standalone.Client;
import io.mongock.examples.dynamodb.standalone.DynamoDBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@ChangeUnit(id = "client-initializer", order = "1", author = "mongock")
public class ClientInitializerCU {

	private static final Logger logger = LoggerFactory.getLogger(ClientInitializerCU.class);
	private final static int CLIENTS_COUNTER = 24;


	@BeforeExecution
	public void createTable(DynamoDBMapper mapper, DynamoDB dynamoDB) throws InterruptedException {
		try {
			Table table = dynamoDB.getTable(Client.TABLE_NAME);
			table.describe();
		} catch (ResourceNotFoundException ex) {
			logger.info("Creating table[{}}", Client.TABLE_NAME);
			CreateTableRequest createTableRequest = mapper.generateCreateTableRequest(Client.class);
			Table table = dynamoDB.createTable(createTableRequest.withProvisionedThroughput(new ProvisionedThroughput(50L, 50L)));
			DynamoDBUtils.waitUntilActive(table);
		}
	}

	@RollbackBeforeExecution
	public void removeTable(DynamoDB dynamoDB) {
		try {
			Table table = dynamoDB.getTable(Client.TABLE_NAME);
			table.describe();
			table.delete();
		} catch (ResourceNotFoundException ex) {
			logger.info("Not deleting table[{}} because it doesn't exist", Client.TABLE_NAME);
		}
	}

	@Execution
	public void execution(DynamoDBTransactionItems transactionItems) {

		for(int i = 0 ; i< CLIENTS_COUNTER; i++) {
			Put put = new Put().withTableName(Client.TABLE_NAME).withItem(getClient(i));
			logger.debug("Added element to transactionItems: " + put);
			transactionItems.add(new TransactWriteItem().withPut(put));
		}
	}

	@RollbackExecution
	public void rollbackExecution() {
	}

	private static Map<String, AttributeValue> getClient(int i) {
		Map<String, AttributeValue> clientAtts = new HashMap<>();
		clientAtts.put("client_id", new AttributeValue("id-" + i));
		clientAtts.put("name", new AttributeValue("name-" + i));
		clientAtts.put("email", new AttributeValue("email-" + i));
		clientAtts.put("phone", new AttributeValue("phone" + i));
		clientAtts.put("country", new AttributeValue("country" + i));
		clientAtts.put("deleted", new AttributeValue().withBOOL(false));
		clientAtts.put("counter", new AttributeValue().withN("" + i));
		return clientAtts;
	}
}
