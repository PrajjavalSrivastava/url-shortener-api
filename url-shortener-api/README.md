# URL Shortener REST API

A RESTful URL Shortener API built with Java and Spring Boot. This project generates short codes for long URLs, redirects users to their original destinations, tracks click analytics, supports URL expiration, and provides URL management endpoints.

## Features

- Generate unique short URLs.
- Redirect users using HTTP 302.
- Track click counts and retrieve analytics.
- Retrieve short URL details.
- Delete short URLs.
- Handle expired URLs with HTTP 410 Gone.
- Validate incoming requests.
- Handle errors through a global exception handler.
- Explore endpoints using Swagger/OpenAPI.
- Run automated tests using JUnit 5, Mockito, and MockMvc.

## Tech Stack

- Java
- Spring Boot
- Spring Web
- Spring Data JPA and Hibernate
- MySQL
- Maven
- JUnit 5
- Mockito
- Spring MockMvc
- Swagger/OpenAPI
- Git and GitHub

## Architecture

The application follows a layered architecture:

`Controller → Service → Repository → MySQL Database`

- **Controller:** Handles HTTP requests and responses.
- **Service:** Contains business logic.
- **Repository:** Performs database operations.
- **Entity:** Represents URL records.
- **DTOs:** Define API request and response payloads.

## Prerequisites

- A compatible JDK
- MySQL Server
- Maven, or the Maven Wrapper if available

## Database Configuration

Create a MySQL database named `url_shortener`, or update the JDBC URL in `application.properties` to match your configuration.

Configure these Windows environment variables:

- `DB_USERNAME` — your MySQL username
- `DB_PASSWORD` — your MySQL password

Never commit real passwords or other secrets to GitHub.

## Run the Application

Clone the repository:

```bash
git clone https://github.com/PrajjavalSrivastava/url-shortener-api.git
cd url-shortener-api
```

Configure the database and environment variables, then run the application from Eclipse or use Maven:

```bash
mvnw.cmd spring-boot:run
```

The default application URL is `http://localhost:8080`, unless configured otherwise.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/urls` | Create a short URL |
| GET | `/{shortCode}` | Redirect to the original URL |
| GET | `/api/v1/urls/{shortCode}` | Retrieve URL details |
| GET | `/api/v1/urls/analytics/{shortCode}` | Retrieve click analytics |
| DELETE | `/api/v1/urls/{shortCode}` | Delete a short URL |

Confirm the redirect path against `RedirectController.java` before publishing this table.

## Swagger UI

Start the application and open:

`http://localhost:8080/swagger-ui/index.html`

## Automated Tests

Run the tests with:

```bash
mvnw.cmd test
```

The project has completed a test run with **12 tests passed, zero failures, zero errors, and zero skipped tests**.

## What I Learned

- Designing RESTful APIs
- Implementing HTTP redirects
- Building a layered Spring Boot application
- Managing data using JPA and MySQL
- Handling validation, exceptions, and URL expiration
- Writing unit and controller tests
- Documenting APIs using Swagger/OpenAPI
- Managing configuration with environment variables

## Author

**Prajjaval Srivastava**

- GitHub: https://github.com/PrajjavalSrivastava
- LinkedIn: https://www.linkedin.com/in/prajjaval-srivastava-50ba431aa/
