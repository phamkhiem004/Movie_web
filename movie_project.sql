-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: movie_project
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `actors`
--

DROP TABLE IF EXISTS `actors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `birth_date` date DEFAULT NULL,
  `nationality` varchar(100) DEFAULT NULL,
  `biography` text,
  `avatar_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actors`
--

LOCK TABLES `actors` WRITE;
/*!40000 ALTER TABLE `actors` DISABLE KEYS */;
INSERT INTO `actors` VALUES (1,'Leonardo DiCaprio','1974-11-11','American','Famous actor known for Titanic and Inception.','https://example.com/leonardo.jpg'),(2,'Emma Watson','1990-04-15','British','Actress known for Harry Potter series.','https://example.com/emma.jpg'),(3,'Tom Cruise','1962-07-03','American','Action movie star, Mission Impossible series.','https://example.com/tom.jpg'),(4,'Scarlett Johansson','1984-11-22','American','Marvel Black Widow actress.','https://example.com/scarlett.jpg'),(5,'Chris Hemsworth','1983-08-11','Australian','Actor known for Thor in Marvel.','https://example.com/chris.jpg'),(6,'Jennifer Lawrence','1990-08-15','American','Hunger Games lead actress.','https://example.com/jennifer.jpg'),(7,'Robert Downey Jr.','1965-04-04','American','Iron Man in Marvel Cinematic Universe.','https://example.com/rdj.jpg'),(8,'Keanu Reeves','1964-09-02','Canadian','Known for The Matrix and John Wick.','https://example.com/keanu.jpg'),(9,'Angelina Jolie','1975-06-04','American','Actress and humanitarian.','https://example.com/angelina.jpg'),(10,'Dwayne Johnson','1972-05-02','American','Also known as The Rock, action star.','https://example.com/rock.jpg'),(11,'Tom Holland','1996-06-01','England','Also known as Spider Man, action star.','https://example.com/rock.jpg'),(12,'Tobey Maguire','1975-06-27','America','Also known as Spider Man, action star.','https://example.com/tobey_maguire.jpg');
/*!40000 ALTER TABLE `actors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `episodes`
--

DROP TABLE IF EXISTS `episodes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `episodes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `movie_id` bigint NOT NULL,
  `episode_number` int NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `video_url` varchar(255) NOT NULL,
  `duration` int DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_episode` (`movie_id`,`episode_number`),
  CONSTRAINT `episodes_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `episodes`
--

LOCK TABLES `episodes` WRITE;
/*!40000 ALTER TABLE `episodes` DISABLE KEYS */;
/*!40000 ALTER TABLE `episodes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorite_movies`
--

DROP TABLE IF EXISTS `favorite_movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite_movies` (
  `user_id` bigint NOT NULL,
  `movie_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`movie_id`),
  KEY `movie_id` (`movie_id`),
  CONSTRAINT `favorite_movies_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `favorite_movies_ibfk_2` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorite_movies`
--

LOCK TABLES `favorite_movies` WRITE;
/*!40000 ALTER TABLE `favorite_movies` DISABLE KEYS */;
/*!40000 ALTER TABLE `favorite_movies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genres`
--

DROP TABLE IF EXISTS `genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genres` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genres`
--

LOCK TABLES `genres` WRITE;
/*!40000 ALTER TABLE `genres` DISABLE KEYS */;
INSERT INTO `genres` VALUES (1,'Action'),(2,'Adventure'),(3,'Animation'),(18,'Biography'),(4,'Comedy'),(5,'Crime'),(6,'Documentary'),(7,'Drama'),(17,'Family'),(8,'Fantasy'),(19,'History'),(9,'Horror'),(16,'Musical'),(10,'Mystery'),(11,'Romance'),(12,'Sci-Fi'),(20,'Sport'),(13,'Thriller'),(14,'War'),(15,'Western');
/*!40000 ALTER TABLE `genres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_actors`
--

DROP TABLE IF EXISTS `movie_actors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_actors` (
  `movie_id` bigint NOT NULL,
  `actor_id` bigint NOT NULL,
  `role_name` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`movie_id`,`actor_id`),
  KEY `actor_id` (`actor_id`),
  CONSTRAINT `movie_actors_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`) ON DELETE CASCADE,
  CONSTRAINT `movie_actors_ibfk_2` FOREIGN KEY (`actor_id`) REFERENCES `actors` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_actors`
--

LOCK TABLES `movie_actors` WRITE;
/*!40000 ALTER TABLE `movie_actors` DISABLE KEYS */;
INSERT INTO `movie_actors` VALUES (25,4,'Black Widow'),(25,7,'Iron Man');
/*!40000 ALTER TABLE `movie_actors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_genres`
--

DROP TABLE IF EXISTS `movie_genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_genres` (
  `movie_id` bigint NOT NULL,
  `genre_id` int NOT NULL,
  PRIMARY KEY (`movie_id`,`genre_id`),
  KEY `genre_id` (`genre_id`),
  CONSTRAINT `movie_genres_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`) ON DELETE CASCADE,
  CONSTRAINT `movie_genres_ibfk_2` FOREIGN KEY (`genre_id`) REFERENCES `genres` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_genres`
--

LOCK TABLES `movie_genres` WRITE;
/*!40000 ALTER TABLE `movie_genres` DISABLE KEYS */;
INSERT INTO `movie_genres` VALUES (6,1),(10,1),(16,1),(17,1),(18,1),(25,1),(25,2),(11,4),(8,5),(9,7),(12,7),(7,9),(16,9),(17,9),(18,9),(8,10),(9,11),(14,11),(10,12),(6,13),(7,13);
/*!40000 ALTER TABLE `movie_genres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_views`
--

DROP TABLE IF EXISTS `movie_views`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_views` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `movie_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `viewed_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `ip_address` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `idx_view_movie` (`movie_id`),
  CONSTRAINT `movie_views_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  CONSTRAINT `movie_views_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_views`
--

LOCK TABLES `movie_views` WRITE;
/*!40000 ALTER TABLE `movie_views` DISABLE KEYS */;
/*!40000 ALTER TABLE `movie_views` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` text,
  `trailer_url` varchar(255) DEFAULT NULL,
  `poster_url` varchar(255) DEFAULT NULL,
  `duration` int DEFAULT NULL,
  `release_date` date DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  `language` varchar(100) DEFAULT NULL,
  `age_limit` int DEFAULT NULL,
  `status` enum('COMING_SOON','NOW_SHOWING','STOPPED') DEFAULT 'NOW_SHOWING',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`),
  KEY `updated_by` (`updated_by`),
  KEY `idx_movie_title` (`title`),
  CONSTRAINT `movies_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `movies_ibfk_2` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movies`
--

LOCK TABLES `movies` WRITE;
/*!40000 ALTER TABLE `movies` DISABLE KEYS */;
INSERT INTO `movies` VALUES (6,'The Silent Code','A hacker uncovers a hidden digital conspiracy.','https://example.com/trailer1','https://example.com/poster1',115,'2023-05-12','USA','English',13,'NOW_SHOWING',0,1,NULL,'2026-03-03 22:59:33','2026-03-03 22:59:33'),(7,'The Silent Hill','A hacker uncovers a hidden digital conspiracy.','https://example.com/trailer1','https://example.com/poster1',115,'2023-05-12','England','India',13,'NOW_SHOWING',0,1,NULL,'2026-03-03 22:59:33','2026-03-07 21:47:34'),(8,'Midnight in Tokyo','A detective hunts a mysterious serial killer.','https://example.com/trailer3','https://example.com/poster3',128,'2024-01-20','Japan','Japanese',18,'NOW_SHOWING',0,1,NULL,'2026-03-03 22:59:33','2026-03-03 22:59:33'),(9,'Ocean of Dreams','Two strangers meet on a life-changing journey.','https://example.com/trailer4','https://example.com/poster4',98,'2021-07-18','France','French',13,'NOW_SHOWING',0,1,NULL,'2026-03-03 22:59:33','2026-03-03 22:59:33'),(10,'Galaxy Reborn','Humanity fights for survival in deep space.','https://example.com/trailer5','https://example.com/poster5',140,'2024-08-09','USA','English',13,'NOW_SHOWING',0,1,NULL,'2026-03-03 22:59:33','2026-03-03 22:59:33'),(11,'Example Movie','This is an example movie description.','https://www.youtube.com/watch?v=example','https://www.example.com/poster.jpg',120,'2023-01-01','USA','English',13,'NOW_SHOWING',0,NULL,NULL,'2026-03-05 21:19:35','2026-03-05 21:19:35'),(12,'Example Movie','This is an example movie description.','https://www.youtube.com/watch?v=example','https://www.example.com/poster.jpg',120,'2023-01-01','USA','English',13,'NOW_SHOWING',0,NULL,NULL,'2026-03-05 21:36:01','2026-03-05 21:36:01'),(14,'Example Movie','This is an example movie description.','https://www.youtube.com/watch?v=example','https://www.example.com/poster.jpg',120,'2023-01-01','USA','English',13,'NOW_SHOWING',0,NULL,NULL,'2026-03-05 21:50:41','2026-03-05 21:50:41'),(16,'Resident Evil','A hacker uncovers a hidden digital conspiracy.','https://example.com/trailer12','https://example.com/poster12',115,'2023-05-12','USA','English',18,'NOW_SHOWING',0,NULL,NULL,'2026-03-05 22:10:46','2026-03-05 22:10:46'),(17,'Resident Evil II','A hacker uncovers a hidden digital conspiracy.','https://example.com/trailer13','https://example.com/poster13',139,'2023-05-12','USA','English',18,'NOW_SHOWING',0,NULL,NULL,'2026-03-06 22:18:18','2026-03-06 22:18:18'),(18,'Resident Evil III','?','https://example.com/trailer13','https://example.com/poster13',139,'2023-05-12','USA','English',18,'NOW_SHOWING',0,NULL,NULL,'2026-03-07 21:35:50','2026-03-07 21:35:50'),(20,'Resident Evil III','?','https://example.com/trailer13','https://example.com/poster13',139,'2023-05-12','USA','English',18,'NOW_SHOWING',0,NULL,NULL,'2026-03-10 15:13:54','2026-03-10 15:13:54'),(25,'Avengers','Superhero movie','https://youtube.com/...','https://image.com/...',139,NULL,'England','English',18,'NOW_SHOWING',0,NULL,NULL,'2026-04-18 23:22:59','2026-04-18 23:22:59');
/*!40000 ALTER TABLE `movies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `subscription_id` bigint NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  `payment_status` enum('PENDING','SUCCESS','FAILED') DEFAULT NULL,
  `transaction_code` varchar(150) DEFAULT NULL,
  `paid_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `subscription_id` (`subscription_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`subscription_id`) REFERENCES `subscriptions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMIN','2026-03-03 22:57:35'),(2,'USER','2026-04-16 22:17:50');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription_plans`
--

DROP TABLE IF EXISTS `subscription_plans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscription_plans` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `duration_days` int NOT NULL,
  `max_devices` int DEFAULT '1',
  `quality` enum('SD','HD','FULLHD','4K') DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription_plans`
--

LOCK TABLES `subscription_plans` WRITE;
/*!40000 ALTER TABLE `subscription_plans` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscription_plans` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscriptions`
--

DROP TABLE IF EXISTS `subscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscriptions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `plan_id` int NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `status` enum('ACTIVE','EXPIRED','CANCELLED') DEFAULT 'ACTIVE',
  `auto_renew` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `plan_id` (`plan_id`),
  KEY `idx_subscription_user` (`user_id`),
  CONSTRAINT `subscriptions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `subscriptions_ibfk_2` FOREIGN KEY (`plan_id`) REFERENCES `subscription_plans` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriptions`
--

LOCK TABLES `subscriptions` WRITE;
/*!40000 ALTER TABLE `subscriptions` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(150) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `role_id` int NOT NULL,
  `status` enum('ACTIVE','BLOCKED') DEFAULT 'ACTIVE',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  KEY `role_id` (`role_id`),
  KEY `idx_user_email` (`email`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin@gmail.com','123456',NULL,NULL,1,'ACTIVE',0,'2026-03-03 22:59:26','2026-04-16 22:33:25',NULL,NULL),(2,'tula5904','khiem@gmail.com','123456',NULL,NULL,2,'ACTIVE',NULL,NULL,'2026-04-16 22:51:31',NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `watch_history`
--

DROP TABLE IF EXISTS `watch_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `watch_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `movie_id` bigint NOT NULL,
  `episode_id` bigint DEFAULT NULL,
  `watched_seconds` int DEFAULT '0',
  `completed` tinyint(1) DEFAULT '0',
  `last_watched_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_watch` (`user_id`,`movie_id`,`episode_id`),
  KEY `movie_id` (`movie_id`),
  KEY `episode_id` (`episode_id`),
  KEY `idx_watch_user` (`user_id`),
  CONSTRAINT `watch_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `watch_history_ibfk_2` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`) ON DELETE CASCADE,
  CONSTRAINT `watch_history_ibfk_3` FOREIGN KEY (`episode_id`) REFERENCES `episodes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `watch_history`
--

LOCK TABLES `watch_history` WRITE;
/*!40000 ALTER TABLE `watch_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `watch_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-18 23:47:06
