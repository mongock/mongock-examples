package com.github.cloudyrock.mongock.examples.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.NonLockGuarded;
import org.springframework.jdbc.core.JdbcTemplate;

@ChangeLog(order = "1")
public class ClientInitializerChangeLog {

    @ChangeSet(id = "user-create-table", order = "001", author = "mongock")
    public void dataInitializer(JdbcTemplate template)  {
        template.execute("" +
                "CREATE TABLE client (" +
                "    id int primary key auto_increment," +
                "    name varchar(255)," +
                "    email varchar(255)" +
                ")");
    }

    @ChangeSet(id = "data-initializer-1", order = "002", author = "mongock")
    public void dataInitializer1(JdbcTemplate template) {
        template.update("INSERT INTO client(name, email) values('name1', 'email1')");
    }


}
