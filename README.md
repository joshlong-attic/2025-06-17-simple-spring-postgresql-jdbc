# README 

Some interesting points: 

* there's a dependency in `pom.xml` for the Spring Boot Docker Compose support that, once enabled, will automatically start `compose.yml` for you on every start of the application. I've disabled that support, so you need to manually run the PostgreSQL container in Docker. At the root of the project, run `docker compose up -d `

* if you use the Docker Compose support (which, again, I've commented out), then you don't need to specify connectivity details (well, not during development, at least). Since we do, I've specified them in `application.properties`. Normally, you'd include these values in a more secure way using Spring Vault (for Hashicorp Vault support), or at least environment variables like `SPRING_DATASOURCE_URL`, which would get normalized to `spring.datasource.url`. 

* I've specified some schema and sample data in `schema.sql` and `data.sql`, respectively. Spring Boot will execute those scripts on startup because I told it to in `application.properties`.

* I've got a simple interface, `DogService`, for which I've provided two implementations: one uses the lower-level `JdbcClient`, and the other uses Spring Data JDBC repositories. It's possible to use MyBatis, JOOQ, JPA, DBI, Jetbrains' Exposed library, etc., with Spring. They all have nice integrations, too, and build upon JDBC.

* I've got a little thing that will run when the program starts, using both defined `DogService` implementations to query for all the `dog` records and print them out.

* Behind the scenes, Spring Boot autoconfigured a Hikari-powered connection pool based on `spring.datasource.*`. I demonstrate that by injecting the auto-configured `DataSource` instance and printing `toString`. 

* Spring Boot supports other connection pools, but the default is Hikari. There are properties you can specify in `application.properties` to finesse things like the connections, test SQL queries, etc. I've got a property (commented out) in `application.properties` that specifies the `test-query` for the connection pool, for example.


