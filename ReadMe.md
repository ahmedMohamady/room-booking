# Room Booking

## Requirements

For building and running the application you need:

- [JDK 17](https://adoptium.net/temurin/releases/?variant=openjdk11&version=17)
- [Maven 3](https://maven.apache.org)

## Running the application locally

to run the application locally by command line

```shell
mvn spring-boot:run
```
# Try it out
you can try the Apis throws the swagger docs at http://localhost:8080/swagger-ui.html

# Database

It uses a H2 in memory database , can be changed easily in the `application.yml` for any other database. also can be access when the applicaion running throw http://localhost:8080/h2-console
also there is attached sql file `resources/sql/import.sql` for schema with some random data

# Email server
It use Gmail mail server . can be changed in the `application.yml`
