package com.github.cloudyrock.mongock.examples.changelogs.premigration;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.PreMigration;
import com.mongodb.client.MongoDatabase;

/**
 * PreMigration ChangeLogs are executed before standard ChangeLogs.
 * The order applies only between PreMigration annotated ChangeLogs.
 */
@PreMigration
@ChangeLog(order = "1")
public class PreMigrationChangeLog {

  @ChangeSet(id = "pre-migration", order = "001", author = "mongock")
  public void preMigration(MongoDatabase mongoDatabase) {
    
    // Creating a temp collection, which will be deleted in PostMigration
    mongoDatabase.createCollection("tempCollection");
  }
}
