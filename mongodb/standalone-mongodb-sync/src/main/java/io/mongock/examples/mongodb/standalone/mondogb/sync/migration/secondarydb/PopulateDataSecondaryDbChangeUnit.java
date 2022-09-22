package io.mongock.examples.mongodb.standalone.mondogb.sync.migration.secondarydb;

import io.mongock.examples.mongodb.standalone.mondogb.sync.StandaloneMongoApp;
import io.mongock.examples.mongodb.standalone.mondogb.sync.product.Product;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.RollbackExecution;
import io.changock.migration.api.annotations.NonLockGuarded;

import com.mongodb.client.MongoDatabase;
import javax.inject.Named;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

import org.bson.Document;

import io.mongock.api.annotations.Execution;

@ChangeUnit(id= "populate-data-secondarydb", order = "4", author = "mongock")
public class PopulateDataSecondaryDbChangeUnit {
  
  public final static int INITIAL_PRODUCTS = 10;

  @Execution
  public void execution(@Named("secondaryDb") @NonLockGuarded MongoDatabase secondaryDb) {
    
    // NOTE: We use this ChangeLog to populate data in secondaryDb for examples to work.
    // But we should only use secondary databases for read, because they won't be managed by Mongock
    // lock and transactions.    
    secondaryDb.getCollection(StandaloneMongoApp.PRODUCTS_COLLECTION_NAME, Product.class)
            .insertMany(IntStream.range(0, INITIAL_PRODUCTS)
                    .mapToObj(PopulateDataSecondaryDbChangeUnit::getProduct)
                    .collect(Collectors.toList()));
  }
  
  @RollbackExecution
  public void rollbackExecution(@Named("secondaryDb") @NonLockGuarded MongoDatabase secondaryDb) {
      
    secondaryDb.getCollection(StandaloneMongoApp.PRODUCTS_COLLECTION_NAME).deleteMany(new Document());
  }
  
  private static Product getProduct(int i) {
    return new Product("product-" + i, 5 * i, 100.25 * i);
  }
}
