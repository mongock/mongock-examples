package com.github.cloudyrock.mongock.examples.changelogs.postmigration;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.PostMigration;
import com.mongodb.client.MongoDatabase;

/**
 * PostMigration ChangeLogs are executed after standard ChangeLogs.
 * The order applies only between PostMigration annotated ChangeLogs.
 */
@PostMigration
@ChangeLog(order = "1")
public class PostMigrationChangeLog {

  @ChangeSet(id = "post-migration", order = "001", author = "mongock")
  public void postMigration(MongoDatabase mongoDatabase) {

    // Deleting the temp collection created in PreMigration
    mongoDatabase.getCollection("tempCollection").drop();
  }
}
