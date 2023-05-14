package io.mongock.examples.couchbase;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import io.mongock.driver.couchbase.driver.CouchbaseDriver;
import io.mongock.examples.couchbase.events.MongockEventListener;
import io.mongock.runner.core.builder.RunnerBuilder;
import io.mongock.runner.core.builder.RunnerBuilderProvider;
import io.mongock.runner.standalone.MongockStandalone;


public class RunnerBuilderProviderImpl implements RunnerBuilderProvider {
    private static final String BUCKET_NAME = "bucket";

    @Override
    public RunnerBuilder getBuilder() {
        Cluster cluster = connect();
        Collection collection = cluster.bucket(BUCKET_NAME).defaultCollection();
        CouchbaseDriver driver = CouchbaseDriver.withDefaultLock(cluster, collection); 
        return MongockStandalone.builder()
                .setDriver(driver)
                .addMigrationScanPackage("io.mongock.examples.couchbase.migration")
                .setMigrationStartedListener(MongockEventListener::onStart)
                .setMigrationSuccessListener(MongockEventListener::onSuccess)
                .setMigrationFailureListener(MongockEventListener::onFail)
                .addDependency(cluster)
                .addDependency(driver.getChangeEntryService())
                .addDependency(collection);
    }
    
    private Cluster connect(){
        return Cluster.connect("couchbase://localhost:11210",
                "Administrator",
                "password");
    }

}
