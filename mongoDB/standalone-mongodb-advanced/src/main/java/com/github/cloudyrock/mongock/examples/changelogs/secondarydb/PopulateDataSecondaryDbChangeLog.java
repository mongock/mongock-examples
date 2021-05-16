package com.github.cloudyrock.mongock.examples.changelogs.secondarydb;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.NonLockGuarded;
import com.github.cloudyrock.mongock.PreMigration;
import com.github.cloudyrock.mongock.examples.product.Product;

import javax.inject.Named;
import java.util.stream.IntStream;

import static com.github.cloudyrock.mongock.examples.StandaloneMongoDbAdvancedApp.PRODUCTS_COLLECTION_NAME;
import com.mongodb.client.MongoDatabase;
import java.util.stream.Collectors;

@PreMigration
@ChangeLog(order = "2")
public class PopulateDataSecondaryDbChangeLog {
  
  public final static int INITIAL_PRODUCTS = 10;

  @ChangeSet(id = "populate-data-secondarydb", order = "001", author = "mongock")
  public void populateDataSecondaryDb(@Named("secondaryDb") @NonLockGuarded MongoDatabase secondaryDb) {
    
    // NOTE: We use this PreMigration ChangeLog to populate data in secondaryDb for examples to work.
    // But we should only use secondary databases for read, because they won't be managed by Mongock
    // lock and transactions.    
    secondaryDb.getCollection(PRODUCTS_COLLECTION_NAME, Product.class)
            .insertMany(IntStream.range(0, INITIAL_PRODUCTS)
                    .mapToObj(PopulateDataSecondaryDbChangeLog::getProduct)
                    .collect(Collectors.toList()));
  }
  
  private static Product getProduct(int i) {
    return new Product("product-" + i, 5 * i, 100.25 * i);
  }
}
