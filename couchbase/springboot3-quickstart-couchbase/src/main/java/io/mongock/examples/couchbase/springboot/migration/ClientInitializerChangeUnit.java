package io.mongock.examples.couchbase.springboot.migration;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.query.DropQueryIndexOptions;
import io.mongock.api.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.couchbase.CouchbaseClientFactory;

import java.util.Arrays;

@ChangeUnit(id = "client-initializer", order = "1", author = "mongock")
public class ClientInitializerChangeUnit {

	private static final Logger logger = LoggerFactory.getLogger(ClientInitializerChangeUnit.class);

	@BeforeExecution
	public void beforeExecution(CouchbaseClientFactory clientFactory) {
		logger.debug("beforeExecution with bucket {}", clientFactory.getBucket().name() );
	}

	@RollbackBeforeExecution
	public void rollbackBeforeExecution(CouchbaseClientFactory clientFactory) {
		logger.debug("rollbackBeforeExecution with bucket {}", clientFactory.getBucket().name() );
	}
	@Execution
	public void execution(Cluster cluster) {
		cluster.queryIndexes().createIndex("bucket", "idx_example_index", Arrays.asList("field1, field2"));
	}

	@RollbackExecution
	public void rollbackExecution(Cluster cluster) {
		cluster.queryIndexes().dropIndex("bucket", "idx_example_index", DropQueryIndexOptions.dropQueryIndexOptions().ignoreIfNotExists(true));
	}

}
