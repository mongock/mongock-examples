package io.mongock.examples.standalone.reactive.client;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Client {

  @BsonId
  private ObjectId id;

  private ZonedDateTime dateTime;

  private String name;

  private String email;

  private String phone;

  private String country;

  private ActivationModel activation;

  private boolean deleted;
  
  private int counter;

  public Client() {
    this.dateTime = ZonedDateTime.now();
  }

  public Client(String name, String email, String phone, String country) {
    this(name, email, phone, country, new ActivationModel());
  }

  public Client(String name, String email, String phone, String country, ActivationModel activation) {
    this();
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.country = country;
    this.activation = activation;
    this.deleted = false;
    this.counter = 0;
  }

  // setters returning 'this' for fluent use in stream. Shouldn't be taken as precedent
  public Client setId(ObjectId id) {
    this.id = id;
    return this;
  }

  public Client setName(String name) {
    this.name = name;
    return this;
  }

  public Client setEmail(String email) {
    this.email = email;
    return this;
  }

  public Client setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public Client setCountry(String country) {
    this.country = country;
    return this;
  }

  public Client setActivation(ActivationModel activation) {
    this.activation = activation;
    return this;
  }

  public Client setDeleted(boolean deleted) {
    this.deleted = deleted;
    return this;
  }

  public Client setDateTime(ZonedDateTime dateTime) {
    this.dateTime = dateTime;
    return this;
  }
  
  public Client setCounter(int counter) {
    this.counter = counter;
    return this;
  }

  public ObjectId getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public String getCountry() {
    return country;
  }

  public ActivationModel getActivation() {
    return activation;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public ZonedDateTime getDateTime() {
    return dateTime;
  }
  
  public int getCounter() {
    return counter;
  }


  @Override
  public String toString() {
    return "Client{" +
            "name='" + name + '\'' +
            '}';
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Client client = (Client) o;
    return deleted == client.deleted &&
            counter == client.counter &&
            Objects.equals(id, client.id) &&
            Objects.equals(dateTime, client.dateTime) &&
            Objects.equals(name, client.name) &&
            Objects.equals(email, client.email) &&
            Objects.equals(phone, client.phone) &&
            Objects.equals(country, client.country) &&
            Objects.equals(activation, client.activation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dateTime, name, email, phone, country, activation, deleted, counter);
  }
}
