package com.github.cloudyrock.mongock.examples.mysql.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

@ChangeLog(order = "1")
public class ClientInitializerChangeLog {

    public final static int INITIAL_CLIENTS = 10;


    @ChangeSet(id = "DDL-create-table", order = "001", author = "mongock")
    public void createTable(Connection connection) throws SQLException {
        //As Mysql handles DDL operations when there is a transaction on going(actually it executes it outside the transaction),
        //it's fine leave it here
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE `table_example` (" +
                            "`id` int NOT NULL AUTO_INCREMENT," +
                            "`name` varchar(100) DEFAULT NULL," +
                            "PRIMARY KEY (`id`)\n" +
                            ") "
            );
        }
    }

    @ChangeSet(id = "data-initializer", order = "002", author = "mongock")
    public void dataInitializer(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    String.format("INSERT INTO table_example(name) values('basic-example-%s');", LocalDateTime.now())
            );
        }
    }

}
