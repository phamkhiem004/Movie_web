# 🎬 Chill Movie Platform - Backend API

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-blue)
![Redis](https://img.shields.io/badge/Redis-Cache-red)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-Streaming-black)
![Docker](https://img.shields.io/badge/Docker-Container-2496ED)
![Maven](https://img.shields.io/badge/Build-Maven-informational)

This is the robust backend RESTful API for the **Chill Movie** platform, built with Spring Boot. It provides comprehensive services for managing movies, user authentication, high-performance caching, asynchronous event processing, and templated email notifications.

---

## ✨ Features

* **🔐 Authentication & Authorization:**
  * Stateless session management using **JWT (JSON Web Tokens)**.
  * Role-based access control (`ROLE_ADMIN`, `ROLE_USER`).
  * Secure password hashing using BCrypt.
  * **⚡ High-Performance:** Fast user authentication and token validation using **Redis** caching.
* **🎥 Movie & Actor Management:**
  * CRUD operations for Movies, Actors, and Genres (Admin only).
  * Public endpoints for searching and viewing movie/actor details.
  * **📊 Event-Driven Tracking:** Asynchronous processing of "watch movie" events via **Apache Kafka**.
* **⭐ User Features:**
  * User registration and profile management.
  * Add/Remove favorite movies.
* **📧 Asynchronous Email Service:**
  * Event-driven email sending flow powered by **Apache Kafka** to ensure non-blocking API responses.
  * Automated email sending using Google SMTP (`JavaMailSender`).
  * Beautiful, dynamic HTML email templates (e.g., account verification) rendered with **Thymeleaf**.
* **📖 API Documentation:**
  * Auto-generated, interactive API documentation using **Swagger (OpenAPI 3)**.

---

## 🛠️ Technology Stack

* **Core:** Java 17+, Spring Boot 3
* **Security:** Spring Security, Nimbus JOSE + JWT
* **Database & Caching:** SQL (MySQL/PostgreSQL), Spring Data JPA, **Redis**
* **Message Broker:** **Apache Kafka**
* **Template Engine:** **Thymeleaf** (for Email)
* **DevOps & Build:** **Docker**, Maven (Multi-environment Profiles)
* **API Documentation:** SpringDoc OpenAPI (Swagger UI)

---

## 🌍 Environments & Maven Profiles

This project is configured with three distinct Maven profiles to manage different deployment environments:

* **`dev` (Development):** Used for local development. Connects to local database, Redis, and Kafka. Logs are set to `DEBUG`.
* **`test` (Testing):** Used for CI/CD pipelines and running integration tests. Might use in-memory databases or embedded services.
* **`prod` (Production):** Optimized for production. Enforces strict security, connects to production containerized infrastructure, and logs only `INFO` and `ERROR`.

---

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing.

### 1. Prerequisites
* Java Development Kit (JDK) 17 or higher.
* Maven 3.6+ installed.
* **Docker & Docker Compose** installed and running (for infrastructure).

### 2. Infrastructure Setup (Docker)
Instead of installing MySQL, Redis, and Kafka manually, use Docker Compose to spin up the infrastructure:

```bash

# Start MySQL, Redis, Zookeeper, and Kafka in the background
docker-compose up -d
3. Application Configuration
Locate the src/main/resources/application-dev.yml (or .properties) file and update the following environment variables:

Properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/movie_project
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# JWT Secret Key (Base64 Encoded)
khiem.jwt.base64-secret=YOUR_BASE64_ENCODED_SECRET_KEY_HERE

# Email SMTP Configuration (Google App Password required)
spring.mail.username=your_email@gmail.com
spring.mail.password=your_google_app_password
(Note: Never commit your actual App Passwords or JWT Secrets to a public repository).

4. Build and Run
Option A: Running Locally via Maven (Dev Profile)
You can run the application directly using the Maven wrapper, specifying the dev profile:

Bash
# Clean and build the project for the 'dev' environment
./mvnw clean install -Pdev

# Run the Spring Boot application using the 'dev' profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
Option B: Running via Docker (Prod Profile)
To package the application and run it as a Docker container for production:

Bash
# 1. Build the production executable JAR
./mvnw clean package -Pprod -DskipTests

# 2. Build the Docker image
docker build -t chill-movie-api:latest .

# 3. Run the application using Docker
docker run -d -p 8080:8080 --name chill-movie-app chill-movie-api:latest
📚 API Documentation
Once the application is running, you can access the interactive API documentation and test the endpoints directly via Swagger UI:

Swagger UI: http://localhost:8080/swagger-ui.html

OpenAPI JSON: http://localhost:8080/v3/api-docs

An exported Postman/OpenAPI collection is also available in the repository as api-document.json.
