package io.mongock.examples.mongodb.springboot.advance.config;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.mongock.examples.mongodb.springboot.advance.spring.DateToZonedDateTimeConverter;
import io.mongock.examples.mongodb.springboot.advance.spring.ZonedDateTimeToDateConverter;
import io.mongock.examples.mongodb.springboot.advance.client.ClientRepository;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
@EnableMongoRepositories(basePackageClasses = ClientRepository.class)
public class MongoDbCommonConfiguration {

	public final static String MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/";
	public final static String MONGODB_MAIN_DB_NAME = "test";


	/**
	 * Injecting MongoDatabase beans we can access mongodb secondary databases.
	 * Secondary database should be used only for read, because it won't be managed by Mongock.
	 * We can inject it in ChangeSets this way:
	 * @Named("secondaryDb") @NonLockGuarded MongoDatabase secondaryDb
	 * In this example we are connecting to a single mongodb server, but we could
	 * connect to a different server in the same way.
	 */
	@Bean("secondaryDb")
	public MongoDatabase secondaryDb() {
		MongoClient mongoClient = this.buildMongoClientWithCodecs(MONGODB_CONNECTION_STRING);
		return mongoClient.getDatabase("secondaryDb");
	}

	/**
	 * Also we can inject MongoTemplate as a bean to access mongodb secondary databases.
	 * In this case, we need to inject @Primary MongoTemplate named "mongoTemplate" first,
	 * which will be the main MongoTemplate for Mongock to work.
	 */
	@Primary
	@Bean
	public MongoTemplate mongoTemplate(MongoCustomConversions customConversions) {

		MongoClient mongoClient = MongoClients.create(MONGODB_CONNECTION_STRING);
		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGODB_MAIN_DB_NAME);

		// Custom converters to map ZonedDateTime.
		MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
		mongoMapping.setCustomConversions(customConversions);
		mongoMapping.afterPropertiesSet();

		return mongoTemplate;
	}

	/**
	 * Secondary MongoTemplate.
	 * Secondary template should be used only for read, because it won't be managed by Mongock.
	 * We can inject it in ChangeSets this way:
	 * @Named("secondaryMongoTemplate") @NonLockGuarded MongoTemplate secondaryTemplate
	 * In this example we are connecting to a single mongodb server, but we could
	 * connect to a different server in the same way.
	 */
	@Bean("secondaryMongoTemplate")
	public MongoTemplate secondaryMongoTemplate(MongoCustomConversions customConversions) {

		MongoClient mongoClient = MongoClients.create(MONGODB_CONNECTION_STRING);
		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "secondaryDb");

		// Custom converters to map ZonedDateTime.
		MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
		mongoMapping.setCustomConversions(customConversions);
		mongoMapping.afterPropertiesSet();

		return mongoTemplate;
	}

	/**
	 * Custom converters to map ZonedDateTime.
	 */
	@Bean
	public MongoCustomConversions customConversions() {
		List<Converter<?, ?>> converters = new ArrayList<>();
		converters.add(DateToZonedDateTimeConverter.INSTANCE);
		converters.add(ZonedDateTimeToDateConverter.INSTANCE);
		return new MongoCustomConversions(converters);
	}

	/**
	 * Helper to create MongoClients customized including Codecs
	 */
	private MongoClient buildMongoClientWithCodecs(String connectionString) {

		CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		return MongoClients.create(MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(connectionString))
				.codecRegistry(codecRegistry)
				.build());
	}
}
