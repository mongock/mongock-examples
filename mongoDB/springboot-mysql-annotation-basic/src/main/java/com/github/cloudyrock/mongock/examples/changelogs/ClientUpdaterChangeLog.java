package com.github.cloudyrock.mongock.examples.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.NonLockGuarded;
import org.springframework.jdbc.core.JdbcTemplate;

@ChangeLog(order = "2")
public class ClientUpdaterChangeLog {

    @ChangeSet(id = "data-updater-1", order = "001", author = "mongock")
    public void dataInitializer1(@NonLockGuarded JdbcTemplate template) {
        template.update("INSERT INTO client(name, email) values('name2', 'email2')");
    }

}
