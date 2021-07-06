package com.github.cloudyrock.mongock.examples.changelogs.secondarydb;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.NonLockGuarded;
import com.github.cloudyrock.mongock.examples.product.Product;

import javax.inject.Named;
import java.util.stream.IntStream;

import static com.github.cloudyrock.mongock.examples.SpringBootSpringDataAnnotationAdvancedApp.PRODUCTS_COLLECTION_NAME;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeLog(order = "4")
public class PopulateDataSecondaryDbChangeLog {
  
  public final static int INITIAL_PRODUCTS = 10;

  @ChangeSet(id = "populate-data-secondarydb", order = "001", author = "mongock")
  public void populateDataSecondaryDb(@Named("secondaryMongoTemplate") @NonLockGuarded MongoTemplate secondaryTemplate) {
    
    // NOTE: We use this ChangeLog to populate data in secondaryDb for examples to work.
    // But we should only use secondary databases for read, because they won't be managed by Mongock
    // lock and transactions.
    IntStream.range(0, INITIAL_PRODUCTS)
             .mapToObj(PopulateDataSecondaryDbChangeLog::getProduct)
             .forEach(product -> secondaryTemplate.insert(product, PRODUCTS_COLLECTION_NAME));
  }
  
  private static Product getProduct(int i) {
    return new Product("product-" + i, 5 * i, 100.25 * i);
  }
}
