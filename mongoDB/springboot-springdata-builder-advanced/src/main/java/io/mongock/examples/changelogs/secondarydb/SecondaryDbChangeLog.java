package io.mongock.examples.changelogs.secondarydb;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import io.changock.migration.api.annotations.NonLockGuarded;
import io.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import io.mongock.examples.product.Product;

import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Named;
import java.util.List;

import static com.github.cloudyrock.mongock.examples.SpringBootSpringDataAnnotationAdvancedApp.PRODUCTS_COLLECTION_NAME;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ChangeLog(order = "5")
public class SecondaryDbChangeLog {
  
  @ChangeSet(id = "secondarydb-with-mongodatabase", order = "001", author = "mongock")
  public void secondaryDbWithMongoDatabase(MongockTemplate mongockTemplate, @Named("secondaryDb") @NonLockGuarded MongoDatabase secondaryDb) {

    // To show an example of reading secondary database and writing in main database, we
    // are going to migrate Products from secondaryDb (with MongoDatabase) to main db (with MongockTemplate).
    List<Product> products = StreamSupport.stream(secondaryDb.getCollection(PRODUCTS_COLLECTION_NAME, Product.class).find().spliterator(), false)
                                          .collect(Collectors.toList());
    
    // We have the read products, so now we'll insert them into the main database.
    products.forEach(product -> mongockTemplate.insert(product, "productsWithMongoDatabase"));
  }
  
  @ChangeSet(id = "secondarydb-with-mongotemplate", order = "002", author = "mongock")
  public void secondaryDbWithMongoTemplate(MongockTemplate mongockTemplate, @Named("secondaryMongoTemplate") @NonLockGuarded MongoTemplate secondaryTemplate) {
    
    // To show an example of reading secondary database and writing in main database, we
    // are going to migrate Products from secondaryDb (with MongoTemplate) to main db (with MongockTemplate).
    List<Product> products = secondaryTemplate.findAll(Product.class, PRODUCTS_COLLECTION_NAME);
    
    // We have the read products, so now we'll insert them into the main database.
    products.forEach(product -> mongockTemplate.insert(product, "productsWithMongoTemplate"));
  }
}
