# Temperature Sensor API

Simple REST API designed to save temperature data in a streamed or batched way and fetch it grouping by days or hours.

## Technologies
- Java 17
- Spring Boot
- H2
- Maven
- Docker

## Running

### With Docker
On the root directory, run the commands:
- `docker build -t temperature-sensor-api:latest .`
- `docker run -p 8080:8080 temperature-sensor-api`

### Locally
On the root directory, run the command:
`mvn spring-boot:run`

## API Documentation
The documentation can be accessed on `http://localhost:8080/swagger-ui.html` after running the application.

## Sample Collection
A sample postman collection can be imported using the file temperature-sensor-api.postman_collection.json present in the root directory.