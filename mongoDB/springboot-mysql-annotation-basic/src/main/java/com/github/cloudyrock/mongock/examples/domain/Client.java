package com.github.cloudyrock.mongock.examples.domain;


import org.springframework.data.annotation.Id;

public class Client {

    public Client() {
    }

    public Client(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Id
    private Integer id;

    private String name;

    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
