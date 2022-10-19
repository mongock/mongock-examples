# Mongock Example for: springboot + spring data

## Table of content
- [Requirements](#requirements)
- [Scope](#scope)
- [Mongock Autoconfiguration vs Builder](#mongock-autoconfiguration-vs-builder)
- [CLI](#cli)


## Requirements
- Java 8
- Maven 3.x
- MongoDB with ReplicaSet(You can easily setup a MongoDB replicaset with this [repository](https://github.com/mongock/mongodb-replset-deployment-docker))

## Scope
- How to use Mongock with Springboot and Spring data MongoDB
- How to use Mongock with both alternative approaches: autoconfiguration and builder
- How to use a secondary database(in this case MongoDB, but could be any) to retrieve data(shouldn't be used as migration-target database)
- How to use the CLI to run the migration

## Mongock Autoconfiguration vs Builder
Mongock offers these two configuration options with Springboot. Only one of them should be enabled at a time.

In this example we should how to use the builder approach, as it's a bit more cumbersome. However in `io.mongock.examples.config.MongockConfiguration.java` can be found the easy steps you need to enable the autoconfiguration.
Also in the module `springboot-quickstart` in this multiproject, you can find an example with Mongock autconfiguration.

## CLI
<!--  Remove this section with just the documentation link: https://docs.mongock.io/cli-->
To use the CLI you don't need to do anything else in this application, just install the CLI in two very easy steps and pass the application as a parameter, so it can take the migration and the dependencies.

### Install CLI
1. Download the latest version of the **mongock-cli-LATEST_VERSION.zip** from [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.mongock/mongock-cli/badge.png)](https://repo.maven.apache.org/maven2/io/mongock/mongock-cli/)
2. Unzip it

### MongockCliConfiguration
You can optionally use the `@MongockCliConfiguration` to tell the CLI explicitly the configuration classes to load, instead of loading the entire context. For example, you don't want your MongockEvent listeners to be processed with the CLI, like in this case.

```java
@MongockCliConfiguration(sources = {
        MongoDbCommonConfiguration.class,
        MongockConfiguration.class
})
//...
public class SpringBootAutoconfigurationApp {
//...
}
```

### Run the migration with the CLI

1. Execute `mvn clean package` inside your application folder.
2. Open a terminal and locate it inside the unzipped folder from the installation step
3. Execute `./mongock migrate -aj YOUR_PROJECT_FOLDER/target/springboot-advance-5.1.5-SNAPSHOT.jar`

> :bulb: The Mongock CLI requires an uber application jar. **Luckily for Springboot users, this is provided by the framework out of the box**


The output should look similar to this:

![cli migrate](./images/cli-migrate.png)


