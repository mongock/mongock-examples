package io.mongock.examples.changelogs.secondarydb;

import io.mongock.examples.product.Product;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import io.changock.migration.api.annotations.NonLockGuarded;

import com.mongodb.client.MongoDatabase;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import static io.mongock.examples.SpringBootSpringDataBuilderAdvancedApp.PRODUCTS_COLLECTION_NAME;

@ChangeUnit(id="secondarydb-with-mongodatabase", order = "5", author="mongock")
public class SecondaryDbWithMongoDatabaseChangeLog {
  
  @Execution
  public void execution(MongoTemplate mongoTemplate, @Named("secondaryDb") @NonLockGuarded MongoDatabase secondaryDb) {

    // To show an example of reading secondary database and writing in main database, we
    // are going to migrate Products from secondaryDb (with MongoDatabase) to main db (with MongockTemplate).
    List<Product> products = StreamSupport.stream(secondaryDb.getCollection(PRODUCTS_COLLECTION_NAME, Product.class).find().spliterator(), false)
                                          .collect(Collectors.toList());
    
    // We have the read products, so now we'll insert them into the main database.
    products.forEach(product -> mongoTemplate.insert(product, "productsWithMongoDatabase"));
  }

  @RollbackExecution
  public void rollbackExecution(MongoTemplate mongoTemplate, @Named("secondaryDb") @NonLockGuarded MongoDatabase secondaryDb) {
    
    mongoTemplate.remove(new Document(), "productsWithMongoDatabase");
  }
}
