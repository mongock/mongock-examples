package com.github.cloudyrock.mongock.examples.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.examples.domain.User;
import com.github.cloudyrock.mongock.examples.repository.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@ChangeLog(order = "1")
public class ClientInitializerChangeLog {

    public final static int INITIAL_CLIENTS = 10;

    @ChangeSet(id = "data-initializer", order = "002", author = "mongock")
    public void dataInitializer(UserRepository repository) throws SQLException {
//        Iterable<User> users = repository.findAll();
        System.out.println(repository.toString());
    }

//    @ChangeSet(id = "data-initializer-2", order = "003", author = "mongock")
//    public void dataInitializer2(Connection connection) throws SQLException {
//        try (Statement statement = connection.createStatement()) {
//            statement.executeUpdate(
//                    String.format("INSERT INTO table_example(name) values('basic-example-2-%s');", LocalDateTime.now())
//            );
//        }
//    }
//
//    @ChangeSet(id = "data-initializer-3", order = "003", author = "mongock")
//    public void dataInitializer23(Connection connection) throws SQLException {
//        if(true) throw new RuntimeException("Expected exception");
//    }

}
