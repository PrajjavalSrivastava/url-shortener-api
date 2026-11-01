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
