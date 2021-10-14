package io.mongock.examples.product;

import java.util.Objects;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class Product {

  @BsonId
  private ObjectId id;

  private String description;
  
  private int stock;
  
  private double price;

  public Product() {
  }

  public Product(String description, int stock, double price) {
    this();
    this.description = description;
    this.stock = stock;
    this.price = price;
  }

  // setters returning 'this' for fluent use in stream. Shouldn't be taken as precedent
  public Product setId(ObjectId id) {
    this.id = id;
    return this;
  }

  public Product setDescription(String description) {
    this.description = description;
    return this;
  }

  public Product setStock(int stock) {
    this.stock = stock;
    return this;
  }

  public Product setPrice(double price) {
    this.price = price;
    return this;
  }

  public ObjectId getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public int getStock() {
    return stock;
  }

  public double getPrice() {
    return price;
  }

  @Override
  public String toString() {
    return "Product{" +
            "description='" + description + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return price == product.price &&
            stock == product.stock &&
            Objects.equals(id, product.id) &&
            Objects.equals(description, product.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, description, stock, price);
  }
}
