package io.mongock.professional.examples.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.mongock.professional.runner.common.multitenant.selector.TenantManager;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;


public class MultiTenantMongoDBFactory extends SimpleMongoClientDatabaseFactory {


    private final TenantManager tenantSelector;

    public MultiTenantMongoDBFactory(MongoClient mongoClient, TenantManager tenantSelector) {
        super(mongoClient, "non-used-db");
        this.tenantSelector = tenantSelector;
    }

    @Override
    public MongoDatabase getMongoDatabase() {
        return getMongoClient().getDatabase(getTenantDatabase());
    }


    protected String getTenantDatabase() {
        String selected = tenantSelector.getSelected();
        if (selected == null) {
            throw new RuntimeException("TenantSelector returning null tenant");
        }
        return selected;
    }
}
