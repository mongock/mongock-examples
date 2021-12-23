# Mongock Example for: springboot + spring data

## Table of content
- [Requirements](#requirements)
- [Scope](#scope)
- [CLI](#cli)


## Requirements
- Java 8
- Maven 3.x
- MongoDB with ReplicaSet(You can easily setup a MongoDB replicaset with this [repository](https://github.com/mongock/mongodb-replset-deployment-docker))

## Scope
- How to use Mongock standalone with  MongoDB springdata
- How to inject a custom dependency into the changeUnit. In this case  a secondary database(we have used  MongoDB, but could be any database or remote system) to retrieve data(shouldn't be used as migration-target database)
- How to use the CLI to run the migration

## CLI
In order to make this application ready to work with the CLI, you need to steps: install the CLI and prepare the app with 3 easy steps.
### Install CLI
<!--  Remove this section with just the documentation link: https://www.mongock.io/cli-->
1. Download the latest version of the **mongock-cli-LATEST_VERSION.zip** from [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.mongock/mongock-cli/badge.png)](https://repo.maven.apache.org/maven2/io/mongock/mongock-cli/)
2. Unzip it

### Prepare the application

#### Implementing the RunnerBuilderProvider interface

The class `RunnerBuilderProviderImpl` implements the interface in this project. The CLI will take the runner from it.

### Annotate the main class with `@MongockCliConfiguration`
```java
@MongockCliConfiguration(sources = RunnerBuilderProviderImpl.class)
public class StandaloneMongoApp {
//...
}
```

### Generate the jar

The CLI needs the application jar build and packed with the jar libraries that will needed in the migration. This means, of course the mongodb library, but also any library from which the changeUnits use classes.

The easies way is to create an uber jar with `maven-shade-plugin`, as it's shown in the pom.xml file.

### Run the migration with the CLI

<!--  Remove this section with just the documentation link: https://www.mongock.io/cli/operations#migrate-->
1. Execute `mvn clean package` inside your application folder.
2. Open a terminal and locate it inside the unzipped folder from the installation step
3. Execute `./mongock migrate -aj YOUR_PROJECT_FOLDER/target/standalone-springdata-1.0-SNAPSHOT.jar`

> :bulb: The Mongock CLI requires an uber application jar. **Luckily for Springboot users, this is provided by the framework out of the box**


<!--The output should look similar to this:

![cli migrate](./images/cli-migrate.png)-->


