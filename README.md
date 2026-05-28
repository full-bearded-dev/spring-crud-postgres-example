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

Run the application for the first time and seed the 
first admin user:

```shell
ADMIN_PASSWORD=supersecure ADMIN_EMAIL=admin@example.com ./gradlew bootRun
```

**NOTE: you can change these admin credentials to 
whatever you'd like. This will only create one user with 
the name "admin" in the Postgres database.**

Run the application after creating an admin user:

```shell
./gradlew bootRun
```

## Testing the Application

You can test this app using the [cURL](https://en.wikipedia.org/wiki/CURL) commands below. You may also prefer to use [Postman](https://www.postman.com/) for
testing APIs which is more user-friendly.

### Run Unit and Integration Tests

Run all tests:
```shell
./gradlew test
```

Run a specific test class (change the package and class name):
```shell
./gradlew test --tests com.example.YourTestClass
```

Run a specific test method (change the package, class and method name):
```shell
./gradlew test --tests com.example.YourTestClass.yourTestMethod
```

### cURL Commands

**NOTE: ensure to update the credentials to whatever 
admin user or user you have created. Users with `ADMIN` 
roles can create, update and delete users. Users with 
the `USER` role can only read users.**

#### Create User

```shell
curl -i -u admin:supersecure -X POST \
-H "Content-Type: application/json" \
-d '{"name": "Jon","email": "jon@example.com", "age": 40, "password": "1aB%2cD$"}' \
http://localhost:8080/api/users
```

#### Get All Users

```shell
curl -i -u admin:supersecure -X GET http://localhost:8080/api/users
```

#### Get Single User by ID

```shell
curl -i -u admin:supersecure -X GET http://localhost:8080/api/users/1
```

#### Update User

```shell
curl -i -u admin:supersecure \
-X PUT \
-H "Content-Type: application/json" \
-d '{"name": "JonUpdated", "email": "jon-updated@example.com", "age": 41}' \
http://localhost:8080/api/users/1
```

#### Delete User

```shell
curl -i -u admin:supersecure -X DELETE http://localhost:8080/api/users/1
```