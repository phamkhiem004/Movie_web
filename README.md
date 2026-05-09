# 🎬 Chill Movie Platform - Backend API

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-blue)
![Maven](https://img.shields.io/badge/Build-Maven-informational)

This is the backend RESTful API for the **Chill Movie** platform, built with Spring Boot. It provides comprehensive services for managing movies, actors, genres, user authentication, and email notifications.

---

## ✨ Features

* **🔐 Authentication & Authorization:**
  * Stateless session management using **JWT (JSON Web Tokens)**.
  * Role-based access control (`ROLE_ADMIN`, `ROLE_USER`).
  * Secure password hashing using BCrypt.
* **🎥 Movie & Actor Management:**
  * CRUD operations for Movies, Actors, and Genres (Admin only).
  * Public endpoints for searching and viewing movie/actor details.
* **⭐ User Features:**
  * User registration and profile management.
  * Add/Remove favorite movies.
* **📧 Email Service:**
  * Automated email sending using Google SMTP (`JavaMailSender`).
  * Supports rich text HTML content and multipart file attachments.
* **📖 API Documentation:**
  * Auto-generated, interactive API documentation using **Swagger (OpenAPI 3)**.

---

## 🛠️ Technology Stack

* **Core:** Java 17+, Spring Boot 3
* **Security:** Spring Security, Nimbus JOSE + JWT
* **Database:** SQL (MySQL/PostgreSQL) & Spring Data JPA
* **API Documentation:** SpringDoc OpenAPI (Swagger UI)
* **Utilities:** Lombok, JavaMailSender
* **Build Tool:** Maven

---

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing.

### 1. Prerequisites
* Java Development Kit (JDK) 17 or higher.
* Maven 3.6+ installed.
* A relational database (e.g., MySQL) installed and running.

### 2. Database Setup
1. Create a new database in your SQL server (e.g., `movie_project`).
2. Import the provided SQL dump file to restore the schema and initial data:
   ```bash
   mysql -u your_username -p movie_project < movie_project.sql
3. Application Configuration
Locate the src/main/resources/application.properties (or .yml) file and update the following environment variables:

Properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/movie_project
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# JWT Secret Key (Base64 Encoded)
khiem.jwt.base64-secret=YOUR_BASE64_ENCODED_SECRET_KEY_HERE

# Email SMTP Configuration (Google App Password required)
spring.mail.username=your_email@gmail.com
spring.mail.from=${spring.mail.username}
spring.mail.password=your_google_app_password
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
(Note: Never commit your actual App Passwords or JWT Secrets to a public repository).

4. Build and Run
You can run the application directly using the Maven wrapper:

Bash
# Build the project
./mvnw clean install

# Run the Spring Boot application
./mvnw spring-boot:run
📚 API Documentation
Once the application is running, you can access the interactive API documentation and test the endpoints directly via Swagger UI:

Swagger UI: http://localhost:8080/swagger-ui.html

OpenAPI JSON: http://localhost:8080/v3/api-docs

An exported Postman/OpenAPI collection is also available in the repository as api-document.json.
