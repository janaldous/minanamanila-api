# Minana Manila API

An e-commerce store API

Swagger/OpenAPI documenation, Spring Boot app, Acutator

# How to start
```
./mvnw clean package
cp target/minanamanila-api-0.0.1-SNAPSHOT.jar src/main/docker
cd src/main/docker
docker-compose build && docker-compose up

```

## How to execute commands on the database app
```
winpty docker exec -it <container id> psql -U <psql username> -W <psql password> <psql database name>
```

(Exclude winpty in non-windows OS)

# How to get database change log
Database change log could be generated with the liquibase maven plugin.

```
liquibase:generateChangeLog
```
Make sure to set database credentials in `liquibase.properties`