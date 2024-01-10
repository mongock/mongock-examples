# Mongock Example for: springboot + spring data couchbase

## Table of content
- [Requirements](#requirements)
- [Scope](#scope)
- [CLI](#cli)

## Requirements
- Java 17
- Maven 3.x
- Docker Compose

## Scope
- Quick guide on how to use Mongock with Springboot with autoconfiguration

## Set up
### Couchbase locally
- Run `docker-compose up`
- Check Couchbase is running fine with logging into web UI at http://localhost:8091/ui/index.html Credentials are Administrator/password

### Run the migration spring boot app

1. Execute `mvn clean package` inside your application folder.
2. Execute `java -jar YOUR_PROJECT_FOLDER/target/springboot-quickstart-couchbase-5.x.x-SNAPSHOT.jar`

