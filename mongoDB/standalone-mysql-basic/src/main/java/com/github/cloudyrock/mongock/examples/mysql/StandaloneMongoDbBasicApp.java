package com.github.cloudyrock.mongock.examples.mysql;

import com.github.cloudyrock.mongock.driver.jdbc.driver.SqlDriverSimple;
import com.github.cloudyrock.mongock.runner.core.executor.MongockRunner;
import com.github.cloudyrock.standalone.MongockStandalone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class StandaloneMongoDbBasicApp {

    private static final String MYSQL_DB_NAME = "mysql";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "password";
    private static final String MYSQL_JDBC_URL = "jdbc:mysql://localhost:3306/mysql";

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        try(Connection connection = getConnection()){
            getStandaloneRunner(connection).execute();
        }

    }


    private static MongockRunner<?> getStandaloneRunner(Connection connection) throws SQLException, ClassNotFoundException {
        return MongockStandalone.builder()
                .setDriver(SqlDriverSimple.MysqlWithDefaultLock(connection, MYSQL_DB_NAME))
                .addChangeLogsScanPackage("com.github.cloudyrock.mongock.examples.mysql.changelogs")
                .addDependency(Connection.class, connection)
//                            .setMigrationStartedListener(MongockEventListener::onStart)
//                            .setMigrationSuccessListener(MongockEventListener::onSuccess)
//                            .setMigrationFailureListener(MongockEventListener::onFail)
                .setTrackIgnored(true)
                .setTransactionEnabled(false)
                .buildRunner();
    }


    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(MYSQL_JDBC_URL, MYSQL_USER, MYSQL_PASSWORD);
    }
}
