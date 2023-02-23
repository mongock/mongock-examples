package io.mongock.examples.couchbase.migration;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.manager.query.DropQueryIndexOptions;
import io.mongock.api.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@ChangeUnit(id = "index-initializer", order = "1", author = "mongock")
public class IndexInitializerChangeUnit {

	private static final Logger logger = LoggerFactory.getLogger(IndexInitializerChangeUnit.class);

	@BeforeExecution
	public void beforeExecution(Collection collection) {
		logger.debug("beforeExecution with bucket {}", collection.bucketName());
	}

	@RollbackBeforeExecution
	public void rollbackBeforeExecution(Collection collection) {
		logger.debug("rollbackBeforeExecution with bucket {}", collection.bucketName());
	}
	@Execution
	public void execution(Cluster cluster) {
		cluster.queryIndexes().createIndex("bucket", "idx_standalone_index", Arrays.asList("field1, field2"));
	}

	@RollbackExecution
	public void rollbackExecution(Cluster cluster) {
		cluster.queryIndexes().dropIndex("bucket", "idx_standalone_index", DropQueryIndexOptions.dropQueryIndexOptions().ignoreIfNotExists(true));
	}

}
