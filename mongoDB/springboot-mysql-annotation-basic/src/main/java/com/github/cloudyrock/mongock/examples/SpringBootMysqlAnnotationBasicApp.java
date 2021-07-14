package com.github.cloudyrock.mongock.examples;


import com.github.cloudyrock.mongock.examples.repository.ClientRepository;
import com.github.cloudyrock.mongock.jdbc.springboot.EnableMongockJdbc;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication
@EnableMongockJdbc
@EnableJdbcRepositories(basePackageClasses = ClientRepository.class)
public class SpringBootMysqlAnnotationBasicApp {

    public static void main(String[] args) {

        SpringApplication.run(SpringBootMysqlAnnotationBasicApp.class);
    }

    @Primary
    @Bean(name = "appDataSource")
    public DataSource dataSource() {
        MysqlDataSource mysqlDS = new MysqlDataSource();
        mysqlDS.setURL("jdbc:mysql://localhost:3306/mysql");
        mysqlDS.setUser("root");
        mysqlDS.setPassword("password");
        mysqlDS.setDatabaseName("mysql");
        return mysqlDS;
    }

    @Bean(name = "applicationJdbcTemplate")
    public JdbcTemplate applicationDataConnection(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

}
