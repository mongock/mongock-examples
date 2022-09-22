package io.mongock.examples.mongodb.springboot.advance.migration.secondarydb;

import io.mongock.examples.mongodb.springboot.advance.application.SpringBootAdvanceApp;
import io.mongock.examples.mongodb.springboot.advance.product.Product;

import javax.inject.Named;
import java.util.stream.IntStream;

import io.changock.migration.api.annotations.NonLockGuarded;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id= "populate-data-secondarydb", order = "4", author = "mongock")
public class PopulateDataSecondaryDbChangeUnit {
  
  public final static int INITIAL_PRODUCTS = 10;

  @Execution
  public void execution(@Named("secondaryMongoTemplate") @NonLockGuarded MongoTemplate secondaryTemplate) {
    
    // NOTE: We use this ChangeLog to populate data in secondaryDb for examples to work.
    // But we should only use secondary databases for read, because they won't be managed by Mongock
    // lock and transactions.
    IntStream.range(0, INITIAL_PRODUCTS)
             .mapToObj(PopulateDataSecondaryDbChangeUnit::getProduct)
             .forEach(product -> secondaryTemplate.insert(product, SpringBootAdvanceApp.PRODUCTS_COLLECTION_NAME));
  }
  
  @RollbackExecution
  public void rollbackExecution(@Named("secondaryMongoTemplate") @NonLockGuarded MongoTemplate secondaryTemplate) {
      
    secondaryTemplate.remove(new Document(), SpringBootAdvanceApp.PRODUCTS_COLLECTION_NAME);
  }
  
  private static Product getProduct(int i) {
    return new Product("product-" + i, 5 * i, 100.25 * i);
  }
}
