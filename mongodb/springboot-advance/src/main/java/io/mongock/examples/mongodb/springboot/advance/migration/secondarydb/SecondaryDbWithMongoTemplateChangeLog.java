package io.mongock.examples.mongodb.springboot.advance.migration.secondarydb;

import io.mongock.examples.mongodb.springboot.advance.product.Product;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import io.changock.migration.api.annotations.NonLockGuarded;

import javax.inject.Named;
import java.util.List;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import static io.mongock.examples.mongodb.springboot.advance.application.SpringBootAdvanceApp.PRODUCTS_COLLECTION_NAME;

@ChangeUnit(id="secondarydb-with-mongotemplate", order = "6", author="mongock")
public class SecondaryDbWithMongoTemplateChangeLog {
  
  @Execution
  public void execution(MongoTemplate mongoTemplate, @Named("secondaryMongoTemplate") @NonLockGuarded MongoTemplate secondaryTemplate) {
    
    // To show an example of reading secondary database and writing in main database, we
    // are going to migrate Products from secondaryDb (with MongoTemplate) to main db (with MongockTemplate).
    List<Product> products = secondaryTemplate.findAll(Product.class, PRODUCTS_COLLECTION_NAME);
    
    // We have the read products, so now we'll insert them into the main database.
    products.forEach(product -> mongoTemplate.insert(product, "productsWithMongoTemplate"));
  }
  
  @RollbackExecution
  public void rollbackExecution(MongoTemplate mongoTemplate, @Named("secondaryMongoTemplate") @NonLockGuarded MongoTemplate secondaryTemplate) {
    
    mongoTemplate.remove(new Document(), "productsWithMongoTemplate");
  }
}
