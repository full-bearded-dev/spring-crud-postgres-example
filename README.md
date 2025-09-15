# Spring Boot CRUD Postgres Example

An example repository for creating a Spring Boot CRUD application that connects to a PostgreSQL database.

## Setup and Running the Application

Install and run Postgres through Docker:

```shell
docker run --name postgres \
  -e POSTGRES_USER=dev \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=test_db \
  -p 5432:5432 \
  -d postgres
```

Verify Docker is running:

```shell
docker ps
```

Run the application:

```shell
./gradlew bootRun
```