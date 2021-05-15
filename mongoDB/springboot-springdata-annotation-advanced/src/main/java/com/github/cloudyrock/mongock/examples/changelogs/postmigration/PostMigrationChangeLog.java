package com.github.cloudyrock.mongock.examples.changelogs.postmigration;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.PostMigration;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;

/**
 * PostMigration ChangeLogs are executed after standard ChangeLogs.
 * The order applies only between PostMigration annotated ChangeLogs.
 */
@PostMigration
@ChangeLog(order = "1")
public class PostMigrationChangeLog {

  @ChangeSet(id = "post-migration", order = "001", author = "mongock")
  public void postMigration(MongockTemplate template) {

    // Deleting the temp collection created in PreMigration
    template.dropCollection("tempCollection");
  }
}
