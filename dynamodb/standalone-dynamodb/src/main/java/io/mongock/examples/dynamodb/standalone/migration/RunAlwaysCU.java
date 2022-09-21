package io.mongock.examples.dynamodb.standalone.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import io.mongock.driver.dynamodb.repository.DynamoDBTransactionItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChangeUnit(id = "client-run-always", order = "2", author = "mongock", runAlways = true)
public class RunAlwaysCU {

	private static final Logger logger = LoggerFactory.getLogger(RunAlwaysCU.class);

	@Execution
	public void execution(DynamoDBTransactionItems transactionItems) {
		logger.info("Client changeUnit runAlways doing nothing...but it could be whatever :)");
	}

	@RollbackExecution
	public void rollbackExecution() {
	}
}
