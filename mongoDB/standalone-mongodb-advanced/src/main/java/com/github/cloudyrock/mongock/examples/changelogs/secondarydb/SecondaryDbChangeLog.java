package com.github.cloudyrock.mongock.examples.changelogs.secondarydb;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.NonLockGuarded;
import com.github.cloudyrock.mongock.examples.product.Product;

import com.mongodb.client.MongoDatabase;

import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.github.cloudyrock.mongock.examples.StandaloneMongoDbAdvancedApp.PRODUCTS_COLLECTION_NAME;

@ChangeLog(order = "5")
public class SecondaryDbChangeLog {
  
  @ChangeSet(id = "secondarydb", order = "001", author = "mongock")
  public void secondaryDbWithMongoDatabase(MongoDatabase mongoDatabase, @Named("secondaryDb") @NonLockGuarded MongoDatabase secondaryDb) {

    // To show an example of reading secondary database and writing in main database, we
    // are going to migrate Products from secondaryDb (with MongoDatabase) to main db (with MongockTemplate).
    List<Product> products = StreamSupport.stream(secondaryDb.getCollection(PRODUCTS_COLLECTION_NAME, Product.class).find().spliterator(), false)
                                          .collect(Collectors.toList());
    
    // We have the read products, so now we'll insert them into the main database.
    mongoDatabase.getCollection(PRODUCTS_COLLECTION_NAME, Product.class).insertMany(products);
  }
}
