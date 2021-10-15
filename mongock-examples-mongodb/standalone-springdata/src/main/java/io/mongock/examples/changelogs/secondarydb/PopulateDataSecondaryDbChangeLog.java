package io.mongock.examples.changelogs.secondarydb;

import io.mongock.examples.product.Product;

import javax.inject.Named;
import java.util.stream.IntStream;

import static io.mongock.examples.StandaloneSpringdataApp.PRODUCTS_COLLECTION_NAME;

import io.changock.migration.api.annotations.NonLockGuarded;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id= "populate-data-secondarydb", order = "4", author = "mongock")
public class PopulateDataSecondaryDbChangeLog {
  
  public final static int INITIAL_PRODUCTS = 10;

  @Execution
  public void execution(@Named("secondaryMongoTemplate") @NonLockGuarded MongoTemplate secondaryTemplate) {
    
    // NOTE: We use this ChangeLog to populate data in secondaryDb for examples to work.
    // But we should only use secondary databases for read, because they won't be managed by Mongock
    // lock and transactions.
    IntStream.range(0, INITIAL_PRODUCTS)
             .mapToObj(PopulateDataSecondaryDbChangeLog::getProduct)
             .forEach(product -> secondaryTemplate.insert(product, PRODUCTS_COLLECTION_NAME));
  }
  
  @RollbackExecution
  public void rollbackExecution(@Named("secondaryMongoTemplate") @NonLockGuarded MongoTemplate secondaryTemplate) {
      
    secondaryTemplate.remove(new Document(), PRODUCTS_COLLECTION_NAME);
  }
  
  private static Product getProduct(int i) {
    return new Product("product-" + i, 5 * i, 100.25 * i);
  }
}
