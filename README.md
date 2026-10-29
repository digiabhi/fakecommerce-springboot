# FakeCommerce API

FakeCommerce is a robust, production-ready RESTful e-commerce backend service built with Spring Boot, Java 21, Spring Data JPA, and MySQL. It provides comprehensive endpoints for managing product catalogs, categories, multi-item customer orders, and customer product reviews, along with standardized API response envelopes, Flyway database migrations, environment variable support, and APM telemetry.

---

## Table of Contents

- [Features & Functionality](#features--functionality)
- [Architecture & Tech Stack](#architecture--tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started & Setup](#getting-started--setup)
  - [1. Clone Repository](#1-clone-repository)
  - [2. Database Configuration](#2-database-configuration)
  - [3. Environment Variables Setup](#3-environment-variables-setup)
  - [4. Build the Project](#4-build-the-project)
  - [5. Run the Application](#5-run-the-application)
  - [6. Running Tests](#6-running-tests)
- [API Documentation](#api-documentation)
  - [Standard Response Format](#standard-response-format)
  - [Category APIs](#category-apis)
  - [Product APIs](#product-apis)
  - [Order APIs](#order-apis)
  - [Review APIs](#review-apis)
- [Error Handling](#error-handling)

---

## Features & Functionality

### 1. Product Catalog Management
- **Full Product Lifecycle:** Create, retrieve, inspect, and delete products.
- **Categorization:** Associate products with specific categories.
- **Rich Product Details:** Retrieve detailed product views including resolved category names and metadata.
- **Filtering & Search:** Query products by category name and retrieve all unique categories with active products.

### 2. Category Management
- **Organized Taxonomies:** Create and maintain product categories.
- **Safe Cascade Constraints:** Protect integrity when referencing categories.

### 3. Advanced Order Processing
- **Order Creation:** Place new orders with multiple product line items and quantities.
- **Granular Item Actions:** Dynamically mutate order contents via item-level actions:
  - `ADD`: Add new products or increase quantity.
  - `REMOVE`: Remove a specific product line item.
  - `INCREMENT`: Increment item quantity by 1.
  - `DECREMENT`: Decrement item quantity by 1 (automatically removes line item if quantity reaches 0).
- **Status Workflow:** Update lifecycle state (`PENDING`, etc.).
- **Order Summaries:** Instant calculation of total items and aggregated monetary price across all line items.

### 4. Product & Order Reviews
- **Customer Feedback:** Submit ratings and comments tied directly to both a purchased product and an order.
- **Flexible Lookups:** Query reviews globally, by specific product ID, or by specific order ID.
- **High-Precision Ratings:** Support decimal ratings (e.g., 4.5 / 5.0).

### 5. Data Safety & Architecture Best Practices
- **Soft Deletion:** Entities implement soft deletion via SQL annotations (`@SQLDelete` and `@SQLRestriction("deleted_at IS NULL")`).
- **Adapter & DTO Pattern:** Clean boundary separation between database entities and API data transfer objects using Stream APIs.
- **Automated Database Migrations:** Version-controlled database schema evolutions using Flyway.
- **Configuration Management:** Native `.env` file parsing via `dotenv-java` with fallback to system properties and `application.yml`.
- **APM Integration:** Ready for New Relic Java Agent monitoring.

---

## Architecture & Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4.1.1
- **Persistence & ORM:** Spring Data JPA, Hibernate
- **Database:** MySQL 8.0+
- **Schema Migrations:** Flyway (`flyway-mysql`)
- **Environment Management:** `dotenv-java`
- **Build Tool:** Gradle (Wrapper provided)
- **Monitoring / APM:** New Relic Java Agent
- **Testing:** JUnit 5, Mockito, Spring Boot Test

---

## Project Structure

```
FakeCommerce/
├── build.gradle                                # Build script and dependency declarations
├── settings.gradle                             # Project settings
├── .env.example                                # Template for environment variables
├── newrelic/                                   # New Relic agent configuration and jar
│   ├── newrelic.jar
│   └── newrelic.yml
├── src/
│   ├── main/
│   │   ├── java/com/example/fakecommerce/
│   │   │   ├── FakeCommerceApplication.java    # Spring Boot main entry point (.env loader)
│   │   │   ├── adapters/                       # Entity-to-DTO Stream adapters
│   │   │   │   ├── OrderAdapter.java
│   │   │   │   └── ReviewAdapter.java
│   │   │   ├── controllers/                    # REST API Controllers
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   └── ReviewController.java
│   │   │   ├── dtos/                           # Request & Response Data Transfer Objects
│   │   │   ├── exceptions/                     # Global exception handlers & custom errors
│   │   │   ├── repositories/                   # Spring Data JPA Repositories
│   │   │   ├── schema/                         # JPA Entities and Enums
│   │   │   │   ├── BaseEntity.java
│   │   │   │   ├── Category.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderProduct.java
│   │   │   │   ├── OrderStatus.java
│   │   │   │   ├── Product.java
│   │   │   │   └── Review.java
│   │   │   ├── services/                       # Business Logic Layer
│   │   │   └── utils/                          # Common API response wrappers
│   │   └── resources/
│   │       ├── application.yml                 # Application configuration
│   │       └── db/migrations/                  # Flyway SQL migrations
│   └── test/                                   # Unit and Integration test suites
```

---

## Prerequisites

Ensure you have the following installed on your machine:
- **Java Development Kit (JDK):** Version 21
- **MySQL Database Server:** Version 8.0 or higher
- **Git**

---

## Getting Started & Setup

### 1. Clone Repository

```bash
git clone https://github.com/your-username/FakeCommerce.git
cd FakeCommerce
```

### 2. Database Configuration

Log into MySQL and create the database schema:

```sql
CREATE DATABASE fakecommerce;
```

### 3. Environment Variables Setup

Copy the sample environment file to create your local `.env`:

```bash
# On Linux / macOS
cp .env.example .env

# On Windows PowerShell
Copy-Item .env.example .env
```

Adjust `.env` variables to match your local setup:

```properties
# Application Configuration
PORT=8080

# Database Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/fakecommerce
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_mysql_password

# New Relic Configuration (Optional for local development)
NEW_RELIC_LICENSE_KEY=
NEW_RELIC_APP_NAME=Spring Boot FakeCommerce
```

### 4. Build the Project

Use the Gradle wrapper to build the application and download dependencies:

```bash
# On Linux / macOS
./gradlew build

# On Windows
.\gradlew.bat build
```

### 5. Run the Application

Start the Spring Boot application (Flyway will automatically execute pending database migrations upon startup):

```bash
# On Linux / macOS
./gradlew bootRun

# On Windows
.\gradlew.bat bootRun
```

The server will be available at `http://localhost:8080`.

### 6. Running Tests

Run the test suite:

```bash
# On Linux / macOS
./gradlew test

# On Windows
.\gradlew.bat test
```

---

## API Documentation

Base URL: `http://localhost:8080/api/v1`

### Standard Response Format

All API responses follow a unified response structure:

#### Success Response
```json
{
  "success": true,
  "message": "Operation description",
  "data": { ... },
  "error": null
}
```

#### Error Response
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "error": "ERROR_CODE"
}
```

---

### Category APIs

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/categories` | Create a new category |
| `GET` | `/api/v1/categories` | Retrieve all active categories |
| `GET` | `/api/v1/categories/{id}` | Retrieve category by ID |
| `DELETE` | `/api/v1/categories/{id}` | Soft delete category by ID |

#### Create Category
- **Endpoint:** `POST /api/v1/categories`
- **Request Body:**
  ```json
  {
    "name": "Electronics"
  }
  ```
- **Response (201 Created):**
  ```json
  {
    "success": true,
    "message": "Category created successfully",
    "data": {
      "id": 1,
      "name": "Electronics",
      "createdAt": "2026-10-29T10:00:00",
      "updatedAt": "2026-10-29T10:00:00",
      "deletedAt": null
    },
    "error": null
  }
  ```

---

### Product APIs

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/products` | Retrieve all active products |
| `GET` | `/api/v1/products/{id}` | Retrieve product by ID |
| `GET` | `/api/v1/products/{id}/details` | Retrieve product with resolved category name |
| `GET` | `/api/v1/products/search?categoryName={name}` | Search products by category name |
| `GET` | `/api/v1/products/categories` | Get all unique category names having products |
| `POST` | `/api/v1/products` | Create a new product |
| `DELETE` | `/api/v1/products/{id}` | Soft delete product by ID |

#### Create Product
- **Endpoint:** `POST /api/v1/products`
- **Request Body:**
  ```json
  {
    "title": "Apple iPhone 17 Pro",
    "description": "Latest flagship smartphone with titanium design.",
    "price": 1199.99,
    "image": "https://example.com/images/iphone17pro.png",
    "categoryId": 1,
    "rating": 4.9
  }
  ```
- **Response (201 Created):**
  ```json
  {
    "success": true,
    "message": "Product created successfully",
    "data": {
      "id": 1,
      "title": "Apple iPhone 17 Pro",
      "description": "Latest flagship smartphone with titanium design.",
      "price": 1199.99,
      "image": "https://example.com/images/iphone17pro.png",
      "rating": 4.9,
      "createdAt": "2026-10-29T10:15:00",
      "updatedAt": "2026-10-29T10:15:00",
      "deletedAt": null
    },
    "error": null
  }
  ```

#### Get Product Details
- **Endpoint:** `GET /api/v1/products/{id}/details`
- **Response (200 OK):**
  ```json
  {
    "success": true,
    "message": "Product details retrieved successfully",
    "data": {
      "id": 1,
      "title": "Apple iPhone 17 Pro",
      "description": "Latest flagship smartphone with titanium design.",
      "price": 1199.99,
      "image": "https://example.com/images/iphone17pro.png",
      "rating": 4.9,
      "category": "Electronics"
    },
    "error": null
  }
  ```

---

### Order APIs

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/orders` | Retrieve all active orders |
| `GET` | `/api/v1/orders/{id}` | Retrieve order by ID with line items |
| `GET` | `/api/v1/orders/{id}/summary` | Retrieve order summary (total items & total price) |
| `POST` | `/api/v1/orders` | Create a new order with items |
| `PUT` | `/api/v1/orders/{id}` | Update order status and modify order items |
| `DELETE` | `/api/v1/orders/{id}` | Soft delete order by ID |

#### Create Order
- **Endpoint:** `POST /api/v1/orders`
- **Request Body:**
  ```json
  {
    "orderItems": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }
  ```
- **Response (201 Created):**
  ```json
  {
    "success": true,
    "message": "Order created successfully",
    "data": {
      "id": 1,
      "status": "PENDING",
      "items": [
        {
          "productId": 1,
          "productName": "Apple iPhone 17 Pro",
          "productImage": "https://example.com/images/iphone17pro.png",
          "productPrice": 1199.99,
          "quantity": 2,
          "subTotal": 2399.98
        }
      ],
      "createdAt": "2026-10-29T11:00:00",
      "updatedAt": "2026-10-29T11:00:00"
    },
    "error": null
  }
  ```

#### Update Order Items / Status
- **Endpoint:** `PUT /api/v1/orders/{id}`
- **Request Body:**
  ```json
  {
    "status": "PENDING",
    "orderItems": [
      {
        "productId": 1,
        "action": "INCREMENT"
      },
      {
        "productId": 2,
        "quantity": 1,
        "action": "ADD"
      }
    ]
  }
  ```
- **Available Actions:**
  - `ADD`: Add product to order (uses `quantity` if specified, defaults to 1).
  - `REMOVE`: Remove product completely from order.
  - `INCREMENT`: Increment product quantity by 1.
  - `DECREMENT`: Decrement product quantity by 1 (removes line item if quantity reaches 0).

#### Get Order Summary
- **Endpoint:** `GET /api/v1/orders/{id}/summary`
- **Response (200 OK):**
  ```json
  {
    "success": true,
    "message": "Order summary retrieved successfully",
    "data": {
      "id": 1,
      "status": "PENDING",
      "orderItems": [
        {
          "productId": 1,
          "productName": "Apple iPhone 17 Pro",
          "productImage": "https://example.com/images/iphone17pro.png",
          "productPrice": 1199.99,
          "quantity": 3,
          "subTotal": 3599.97
        }
      ],
      "totalItems": 3,
      "totalPrice": 3599.97,
      "createdAt": "2026-10-29T11:00:00",
      "updatedAt": "2026-10-29T11:10:00"
    },
    "error": null
  }
  ```

---

### Review APIs

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/reviews` | Create a new review for a product and order |
| `GET` | `/api/v1/reviews` | Retrieve all active reviews |
| `GET` | `/api/v1/reviews/{id}` | Retrieve review by ID |
| `GET` | `/api/v1/reviews/product/{productId}` | Retrieve all reviews for a specific product |
| `GET` | `/api/v1/reviews/order/{orderId}` | Retrieve all reviews for a specific order |
| `DELETE` | `/api/v1/reviews/{id}` | Soft delete review by ID |

#### Create Review
- **Endpoint:** `POST /api/v1/reviews`
- **Request Body:**
  ```json
  {
    "productId": 1,
    "orderId": 1,
    "rating": 5.0,
    "comment": "Outstanding build quality and battery performance!"
  }
  ```
- **Response (201 Created):**
  ```json
  {
    "success": true,
    "message": "Review created successfully",
    "data": {
      "id": 1,
      "productId": 1,
      "orderId": 1,
      "rating": 5.0,
      "comment": "Outstanding build quality and battery performance!",
      "createdAt": "2026-10-29T11:30:00"
    },
    "error": null
  }
  ```

---

## Error Handling

FakeCommerce uses a centralized `@RestControllerAdvice` exception handler (`GlobalExceptionHandler`) to ensure reliable and predictable error responses:

| HTTP Status | Error Code | Trigger Condition |
| :--- | :--- | :--- |
| `400 Bad Request` | `INVALID_REQUEST` | Missing or invalid required fields (e.g., missing review parameters). |
| `404 Not Found` | `RESOURCE_NOT_FOUND` | Queried entity (Product, Order, Category, Review) does not exist or has been soft-deleted. |
| `409 Conflict` | `RESOURCE_DELETION_FAILED` | Deletion could not be completed due to relational constraints or database errors. |
| `500 Internal Server Error` | `INTERNAL_SERVER_ERROR` | Unhandled runtime or database exceptions. |
