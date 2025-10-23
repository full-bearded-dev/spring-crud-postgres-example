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

## Testing the Application

You can test this app using the [cURL](https://en.wikipedia.org/wiki/CURL) commands below. You may also prefer to use [Postman](https://www.postman.com/) for
testing APIs which is more user-friendly.

### Create User

```shell
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Jon","email": "jon@example.com", "age": 40}'
```

### Get All Users

```shell
curl -X GET http://localhost:8080/api/users
```

### Get Single User by ID

```shell
curl -X GET http://localhost:8080/api/users/1
```

### Update User

```shell
curl -X PUT http://localhost:8080/api/users/1 \
-H "Content-Type: application/json" \
-d '{"name": "JonUpdated", "email": "jon-updated@example.com", "age": 41}'
```

### Delete User

```shell
curl -X DELETE http://localhost:8080/api/users/1
```