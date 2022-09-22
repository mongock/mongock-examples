package io.mongock.examples.mongodb.standalone.reactive;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import io.mongock.examples.mongodb.standalone.reactive.events.MongockEventListener;
import io.mongock.examples.mongodb.standalone.reactive.codec.ZonedDateTimeCodec;
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
	public final static String MONGODB_MAIN_DB_NAME = "test";

	@Override
	public RunnerBuilder getBuilder() {
		return MongockStandalone.builder()
				.setDriver(MongoReactiveDriver.withDefaultLock(getMainMongoClient(), MONGODB_MAIN_DB_NAME))
				.addMigrationScanPackage("io.mongock.examples.mongodb.standalone.reactive.migration")
				.setMigrationStartedListener(MongockEventListener::onStart)
				.setMigrationSuccessListener(MongockEventListener::onSuccess)
				.setMigrationFailureListener(MongockEventListener::onFail)
				.setTrackIgnored(true)
				.setTransactionEnabled(true);
	}

	/**
	 * Main MongoClient for Mongock to work.
	 */
	private static MongoClient getMainMongoClient() {
		return buildMongoClientWithCodecs(MONGODB_CONNECTION_STRING);
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
