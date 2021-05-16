package com.github.cloudyrock.mongock.examples.changelogs.premigration;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.PreMigration;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;

/**
 * PreMigration ChangeLogs are executed before standard ChangeLogs.
 * The order applies only between PreMigration annotated ChangeLogs.
 */
@PreMigration
@ChangeLog(order = "1")
public class PreMigrationChangeLog {

  @ChangeSet(id = "pre-migration", order = "001", author = "mongock")
  public void preMigration(MongockTemplate template) {
    
    // Creating a temp collection, which will be deleted in PostMigration
    template.createCollection("tempCollection");
  }
}
