package io.mongock.examples;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import io.mongock.examples.codec.ZonedDateTimeCodec;
import io.mongock.examples.events.MongockEventListener;
import io.mongock.professional.runner.common.multitenant.wrapper.MultiTenant;
import io.mongock.runner.core.builder.RunnerBuilder;
import io.mongock.runner.core.builder.RunnerBuilderProvider;
import io.mongock.runner.standalone.MongockStandalone;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class RunnerBuilderProviderImpl implements RunnerBuilderProvider {

	public final static String MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/";

	@Override
	public RunnerBuilder getBuilder() {

		MongoClient mongoClient = buildMongoClientWithCodecs(MONGODB_CONNECTION_STRING);;
		MultiTenant multiTenant = MultiTenant.withDrivers(
				getDriver(mongoClient, "tenant-1"),
				getDriver(mongoClient, "tenant-2")
		);
		return MongockStandalone.builder()
				.setMultiTenant(multiTenant)
				.addMigrationScanPackage("io.mongock.examples.changelogs")
				.setMigrationStartedListener(MongockEventListener::onStart)
				.setMigrationSuccessListener(MongockEventListener::onSuccess)
				.setMigrationFailureListener(MongockEventListener::onFail)
				.setTrackIgnored(true)
				.setTransactionEnabled(true);
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

		CodecRegistry codecRegistry = fromRegistries(fromCodecs(new ZonedDateTimeCodec()),
				MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		MongoClientSettings.Builder builder = MongoClientSettings.builder();
		builder.applyConnectionString(new ConnectionString(connectionString));
		builder.codecRegistry(codecRegistry);
		MongoClientSettings build = builder.build();
		return MongoClients.create(build);
	}
}
