package io.mongock.examples.dynamodb.standalone;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.TableStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DynamoDBUtils {

	private static final Logger logger = LoggerFactory.getLogger(DynamoDBUtils.class);
	private static final int DEFAULT_WAIT_TIMEOUT = 10 * 60 * 1000;
	private static final int DEFAULT_WAIT_INTERVAL = 20 * 1000;
	private DynamoDBUtils(){}
	public static void waitUntilActive(Table table) throws InterruptedException {

		long startTime = System.currentTimeMillis();
		long endTime = startTime + DEFAULT_WAIT_TIMEOUT;
		while (System.currentTimeMillis() < endTime) {
			try {
				if (TableStatus.ACTIVE.name().equalsIgnoreCase(table.describe().getTableStatus())) {
					return;
				}
			} catch (ResourceNotFoundException ex) {
				logger.info("Waiting for table[{}] to be active", table.getTableName());
			}

			Thread.sleep(DEFAULT_WAIT_INTERVAL);
		}
	}

}
