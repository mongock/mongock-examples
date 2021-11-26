package io.mongock.examples.dynamodb.standalone;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;


@DynamoDBTable(tableName = Client.TABLE_NAME)
public class Client {

  public static final String TABLE_NAME = "CLIENTS";

  @DynamoDBHashKey(attributeName = "client_id")
  private String clientId;

  @DynamoDBAttribute
  private String name;

  @DynamoDBAttribute
  private String email;

  @DynamoDBAttribute
  private String phone;

  @DynamoDBAttribute
  private String country;

  @DynamoDBAttribute
  private boolean deleted;

  @DynamoDBAttribute
  private int counter;

  public Client() {}

  public Client(String clientId, String name, String email, String phone, String country) {
    this.clientId = clientId;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.country = country;
    this.deleted = false;
    this.counter = 0;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
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

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public int getCounter() {
    return counter;
  }

  public void setCounter(int counter) {
    this.counter = counter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Client client = (Client) o;
    return deleted == client.deleted && counter == client.counter && Objects.equals(clientId, client.clientId) && Objects.equals(name, client.name) && Objects.equals(email, client.email) && Objects.equals(phone, client.phone) && Objects.equals(country, client.country);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientId, name, email, phone, country, deleted, counter);
  }
}
