package io.mongock.examples.dynamodb.standalone.migration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.Put;
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

@ChangeUnit(id = "client-run-always", order = "2", author = "mongock", runAlways = true)
public class RunAlwaysChangeUnit {

	private static final Logger logger = LoggerFactory.getLogger(RunAlwaysChangeUnit.class);

	@Execution
	public void execution(DynamoDBTransactionItems transactionItems) {
		logger.info("Client changeUnit runAlways doing nothing...but it could be whatever :)");
	}

	@RollbackExecution
	public void rollbackExecution() {
	}
}
