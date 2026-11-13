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
- Run the application and MySQL database using Docker Compose.
- Persist database records using a Docker named volume.

## Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA and Hibernate
- MySQL 8
- Maven
- JUnit 5
- Mockito
- Spring MockMvc
- Swagger/OpenAPI
- Docker
- Docker Compose
- Git and GitHub

## Architecture

The application follows a layered architecture that separates HTTP handling, business logic, database operations, and supporting components.

### Application Architecture

```text
                    CLIENT
                      |
                HTTP Requests
                      |
          +-----------+-----------+
          |                       |
    UrlController          RedirectController
          |                       |
          +-----------+-----------+
                      |
                  UrlService
                   (Interface)
                      |
                UrlServiceImpl
                 /          \
                /            \
       UrlRepository    ShortCodeGenerator
              |
          UrlMapping
              |
          MySQL Database
```

### Package Responsibilities

| Package | Responsibility |
|---|---|
| controller | Handles REST API requests and HTTP responses |
| service | Defines the URL service interface |
| service.impl | Implements URL creation, redirection, analytics, and deletion logic |
| repository | Handles database operations using Spring Data JPA |
| entity | Defines the URL mapping database entity |
| dto | Defines API request and response objects |
| exception | Contains custom exceptions and global exception handling |
| util | Provides short-code generation functionality |
| config | Contains application configuration |

### Request Processing

1. The client sends an HTTP request to a controller.
2. The controller delegates the operation to the `UrlService` interface.
3. `UrlServiceImpl` executes the business logic.
4. `UrlRepository` interacts with the database.
5. The controller returns the appropriate HTTP response.

The application uses constructor-based dependency injection to connect controllers with the service interface.

## Prerequisites

For local execution:

- Java 17
- MySQL 8
- Maven, or the Maven Wrapper included in the repository

For Docker execution:

- Docker Desktop
- Docker Compose

## Database Configuration

Create a MySQL database named `url_shortener`, or update the JDBC URL in `application.properties` to match your configuration.

The application expects these environment variables:

- `DB_USERNAME` — your MySQL username
- `DB_PASSWORD` — your MySQL password

Never commit real passwords or other secrets to GitHub.

## Run Locally

Clone the repository:

```bash
git clone https://github.com/PrajjavalSrivastava/url-shortener-api.git
cd url-shortener-api
```

Configure the database and set the required environment variables.

For Windows Command Prompt, for example:

```cmd
set DB_USERNAME=your_mysql_username
set DB_PASSWORD=your_mysql_password
```

Run the application:

```cmd
mvnw.cmd spring-boot:run
```

The default local application URL is:

`http://localhost:8080`

## Run Using Docker

The project uses a multi-stage Dockerfile and Docker Compose to run the Spring Boot application and MySQL database in separate containers.

### 1. Create the environment file

In the project root, create a file named `.env`.

Add the following configuration:

```properties
DB_USERNAME=urluser
DB_PASSWORD=replace_with_your_password
DB_ROOT_PASSWORD=replace_with_your_root_password
```

Replace the example values with your own credentials.

**Important:** The `.env` file contains sensitive information and must not be committed to GitHub. It is excluded through `.gitignore`.

### 2. Build and start the containers

Run this command from the project root:

```bash
docker compose up --build -d
```

This command:

- Builds the Spring Boot application Docker image.
- Starts the MySQL container.
- Waits for MySQL to become healthy.
- Starts the Spring Boot application container.
- Runs both services on the Docker Compose network.

### 3. Verify running containers

```bash
docker compose ps
```

Check that:

- `url-shortener-app` is running.
- `url-shortener-mysql` is running and healthy.

### 4. View application logs

```bash
docker compose logs --tail=50 app
```

To follow logs continuously:

```bash
docker compose logs -f app
```

### 5. Access the application

When running through Docker Compose, the application is exposed on host port `8081`.

Application:

`http://localhost:8081`

Swagger UI:

`http://localhost:8081/swagger-ui/index.html`

### 6. Stop and restart containers

Stop the services:

```bash
docker compose stop
```

Start them again:

```bash
docker compose start
```

To remove the containers and recreate them:

```bash
docker compose down
docker compose up -d
```

The named Docker volume `mysql_data` stores MySQL data separately from the containers. It allows database records to survive container stops and recreation.

**Warning:** Avoid `docker compose down -v` unless you intentionally want to delete the database volume and its stored data.

## API Usage Examples

### 1. Create a Short URL

**Endpoint:** `POST /api/v1/urls`

**Request:**

```http
POST /api/v1/urls
Content-Type: application/json
```

```json
{
  "originalUrl": "https://www.example.com/products",
  "expiresAt": "2026-12-31T23:59:59"
}
```

The `expiresAt` field is optional. If omitted or set to `null`, the URL does not have an expiration time.

**Successful Response — 200 OK**

```json
{
  "originalUrl": "https://www.example.com/products",
  "shortCode": "aB12xY"
}
```

### 2. Redirect to Original URL

**Endpoint:** `GET /{shortCode}`

**Example:**

```http
GET /aB12xY
```

**Response:** `302 Found`

The response contains a `Location` header pointing to the original URL.

### 3. Retrieve URL Details

**Endpoint:** `GET /api/v1/urls/{shortCode}`

**Example:**

```http
GET /api/v1/urls/aB12xY
```

**Successful Response — 200 OK**

```json
{
  "shortCode": "aB12xY",
  "originalUrl": "https://www.example.com/products",
  "clickCount": 5,
  "createdAt": "2026-11-01T10:30:00",
  "expiresAt": "2026-12-31T23:59:59"
}
```

### 4. Retrieve Click Analytics

**Endpoint:** `GET /api/v1/urls/analytics/{shortCode}`

**Example:**

```http
GET /api/v1/urls/analytics/aB12xY
```

**Successful Response — 200 OK**

```json
{
  "shortCode": "aB12xY",
  "originalUrl": "https://www.example.com/products",
  "clickCount": 5,
  "createdAt": "2026-11-01T10:30:00",
  "expiresAt": "2026-12-31T23:59:59"
}
```

Retrieving analytics does not increment the click count.

### 5. Delete a Short URL

**Endpoint:** `DELETE /api/v1/urls/{shortCode}`

**Example:**

```http
DELETE /api/v1/urls/aB12xY
```

**Successful Response:** `204 No Content`

### HTTP Error Responses

| Status Code | Description |
|---|---|
| 400 Bad Request | Invalid request or validation failure |
| 404 Not Found | Short URL does not exist |
| 410 Gone | Short URL has expired |

## Swagger UI

For local execution:

`http://localhost:8080/swagger-ui/index.html`

For Docker execution:

`http://localhost:8081/swagger-ui/index.html`

## Automated Tests

Run the tests using the Maven Wrapper:

```cmd
mvnw.cmd test
```

The project has completed a test run with **12 tests passed, zero failures, zero errors, and zero skipped tests**.

The Dockerfile currently uses `-DskipTests` during image construction. Run the test command separately when you want to verify the test suite.

## What I Learned

- Designing RESTful APIs.
- Implementing HTTP redirects.
- Building a layered Spring Boot application.
- Managing data using JPA and MySQL.
- Handling validation, exceptions, and URL expiration.
- Writing unit and controller tests.
- Documenting APIs using Swagger/OpenAPI.
- Managing configuration with environment variables.
- Creating multi-stage Docker images.
- Running application and database containers using Docker Compose.
- Managing persistent database storage using Docker volumes.
- Troubleshooting container startup and database connectivity.

## Author

**Prajjaval Srivastava**

- GitHub: https://github.com/PrajjavalSrivastava
- LinkedIn: https://www.linkedin.com/in/prajjaval-srivastava-50ba431aa/
