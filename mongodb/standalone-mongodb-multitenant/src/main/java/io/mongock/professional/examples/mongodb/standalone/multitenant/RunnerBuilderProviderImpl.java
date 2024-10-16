package io.mongock.professional.examples.mongodb.standalone.multitenant;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import io.mongock.professional.examples.mongodb.standalone.multitenant.codec.ZonedDateTimeCodec;
import io.mongock.professional.examples.mongodb.standalone.multitenant.events.MongockEventListener;
import io.mongock.runner.core.builder.RunnerBuilder;
import io.mongock.runner.core.builder.RunnerBuilderProvider;
import io.mongock.runner.standalone.MongockStandalone;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class RunnerBuilderProviderImpl implements RunnerBuilderProvider {

    public final static String MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/";

    @Override
    public RunnerBuilder getBuilder() {

        MongoClient mongoClient = buildMongoClientWithCodecs(MONGODB_CONNECTION_STRING);

        /*********************************************************************************
        *  NOTE: You must provide a valid LICENSE KEY for Mongock Professional to work.  *
        *        For further details please visit: https://mongock.io/setup-license      *
        **********************************************************************************/
        return MongockStandalone.builder()
                .setLicenseKey("*** PUT YOUR MONGOCK PROFESSIONAL LICENSE KEY HERE ***")
                .setDriverMultiTenant(
                        getDriver(mongoClient, "tenant-1"),
                        getDriver(mongoClient, "tenant-2"))
                .addMigrationScanPackage("io.mongock.professional.examples.mongodb.standalone.multitenant.migration")
                .setMigrationStartedListener(MongockEventListener::onStart)
                .setMigrationSuccessListener(MongockEventListener::onSuccess)
                .setMigrationFailureListener(MongockEventListener::onFail)
                .setTrackIgnored(true)
                .setTransactional(true);
    }


    private MongoSync4Driver getDriver(MongoClient mongoClient, String dbName) {
        MongoSync4Driver driver = MongoSync4Driver.withDefaultLock(mongoClient, dbName);
        driver.enableTransaction();
        return driver;
    }

    /**
     * Helper to create MongoClients customized including Codecs
     */
    private static MongoClient buildMongoClientWithCodecs(String connectionString) {

        CodecRegistry codecRegistry = fromRegistries(CodecRegistries.fromCodecs(new ZonedDateTimeCodec()),
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings.Builder builder = MongoClientSettings.builder();
        builder.applyConnectionString(new ConnectionString(connectionString));
        builder.codecRegistry(codecRegistry);
        MongoClientSettings build = builder.build();
        return MongoClients.create(build);
    }
}
