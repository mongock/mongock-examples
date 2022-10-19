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
- Quick guide on how to use Mongock with Springboot with autoconfiguration
- Quick guide on how to execute the migration with the Mongock CLI

## CLI
To use the CLI you don't need to do anything else in this application, just install the CLI in two very easy steps and pass the application as a parameter, so it can take the migration and the dependencies.

### Install CLI
<!--  Remove this section with just the documentation link: https://docs.mongock.io/cli-->
1. Download the latest version of the **mongock-cli-LATEST_VERSION.zip** from [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.mongock/mongock-cli/badge.png)](https://repo.maven.apache.org/maven2/io/mongock/mongock-cli/)
2. Unzip it



### Run the migration with the CLI

<!--  Remove this section with just the documentation link: https://docs.mongock.io/cli/operations#migrate-->
1. Execute `mvn clean package` inside your application folder.
2. Open a terminal and locate it inside the unzipped folder from the installation step
3. Execute `./mongock migrate -aj YOUR_PROJECT_FOLDER/target/springboot-quickstart-5.1.5-SNAPSHOT.jar`

> :bulb: The Mongock CLI requires an uber application jar. **Luckily for Springboot users, this is provided by the framework out of the box**


<!--The output should look similar to this:

![cli migrate](./images/cli-migrate.png)-->


