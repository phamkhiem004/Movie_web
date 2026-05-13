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
