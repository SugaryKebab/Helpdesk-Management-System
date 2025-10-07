-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: helpdesk
-- ------------------------------------------------------
-- Server version	8.0.39

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
-- Table structure for table `app_user`
--

DROP TABLE IF EXISTS `app_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_user` (
  `id` char(36) NOT NULL,
  `org_id` char(36) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `password` varchar(256) DEFAULT NULL,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `user_role` enum('ADMIN','AGENT','USER','SUPERADMIN') DEFAULT 'USER',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_org_email` (`org_id`,`email`),
  CONSTRAINT `app_user_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `organization` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_user`
--

LOCK TABLES `app_user` WRITE;
/*!40000 ALTER TABLE `app_user` DISABLE KEYS */;
INSERT INTO `app_user` VALUES ('5a31a87f-b4af-4bb5-9867-9469791ad2be','36eacbc4-77c9-4360-9d78-99d262ebd5be','testuser@helpdesk.com',1,NULL,'$2a$10$yjTS383uvSTf9VBbmlaX4uPiGH5mh97D0fOwSCdpDUDSS8Rr0TRVW','Test','User','ADMIN'),('c0a80103-99b3-18ce-8199-b3d9a7a90002','36eacbc4-77c9-4360-9d78-99d262ebd5be','testuser1@helpdesk.com',1,NULL,'$2a$10$VBz2uG8kfkX65FCfo37w7uugkBpNAt307udFMKCXXrltA/JrjFtHu','Test','User','ADMIN'),('c0a80103-99bd-114b-8199-bdb331200000','36eacbc4-77c9-4360-9d78-99d262ebd5be','newuser@example.com',NULL,NULL,'$2a$10$7WHoM9yIOG/5FGzabRB9fevLAUn3KUteFsRjUbh2wBpDeDINBg8Oy','New','User','USER');
/*!40000 ALTER TABLE `app_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attachment` (
  `id` char(36) NOT NULL,
  `org_id` char(36) NOT NULL,
  `ticket_id` char(36) NOT NULL,
  `uploader_id` char(36) DEFAULT NULL,
  `filename` varchar(255) NOT NULL,
  `mime_type` varchar(100) NOT NULL,
  `byte_size` bigint NOT NULL,
  `storage_key` varchar(500) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`),
  KEY `ticket_id` (`ticket_id`),
  KEY `uploader_id` (`uploader_id`),
  CONSTRAINT `attachment_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `organization` (`id`) ON DELETE CASCADE,
  CONSTRAINT `attachment_ibfk_2` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`id`) ON DELETE CASCADE,
  CONSTRAINT `attachment_ibfk_3` FOREIGN KEY (`uploader_id`) REFERENCES `app_user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment`
--

LOCK TABLES `attachment` WRITE;
/*!40000 ALTER TABLE `attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_event`
--

DROP TABLE IF EXISTS `audit_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_event` (
  `id` char(36) NOT NULL,
  `org_id` char(36) NOT NULL,
  `actor_user_id` char(36) DEFAULT NULL,
  `entity_type` varchar(50) NOT NULL,
  `entity_id` char(36) NOT NULL,
  `action` varchar(50) NOT NULL,
  `data` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`),
  KEY `actor_user_id` (`actor_user_id`),
  CONSTRAINT `audit_event_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `organization` (`id`) ON DELETE CASCADE,
  CONSTRAINT `audit_event_ibfk_2` FOREIGN KEY (`actor_user_id`) REFERENCES `app_user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_event`
--

LOCK TABLES `audit_event` WRITE;
/*!40000 ALTER TABLE `audit_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` char(36) NOT NULL,
  `org_id` char(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `parent_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_category_org_name` (`org_id`,`name`),
  KEY `parent_id` (`parent_id`),
  CONSTRAINT `category_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `organization` (`id`) ON DELETE CASCADE,
  CONSTRAINT `category_ibfk_2` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES ('85de99f4-6350-4792-86c3-5726fd4f95cf','36eacbc4-77c9-4360-9d78-99d262ebd5be','Support Team',NULL),('9014da7f-72fd-4e07-8821-e3d502e1f021','36eacbc4-77c9-4360-9d78-99d262ebd5be','Support Team3',NULL);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organization` (
  `id` char(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
INSERT INTO `organization` VALUES ('36eacbc4-77c9-4360-9d78-99d262ebd5be','Hey','2025-09-30 14:34:01'),('c0a80103-99b8-1562-8199-b8e6363b0000','Acme Corp2','2025-10-06 06:41:59'),('c0a80103-99b8-1562-8199-b8e7fb000002','Acme Corp4','2025-10-06 06:43:55'),('c0a80103-99b8-15d2-8199-b8f62ab80000','Acme Corp3','2025-10-06 06:59:25'),('c0a80103-99b8-1b91-8199-b8dbab460000','Acme Corp',NULL);
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_token`
--

DROP TABLE IF EXISTS `password_reset_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_token` (
  `id` varchar(36) NOT NULL,
  `token` varchar(255) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  `expires_at` datetime NOT NULL,
  `used` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_token` (`token`),
  KEY `fk_token_user` (`user_id`),
  CONSTRAINT `fk_token_user` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_token`
--

LOCK TABLES `password_reset_token` WRITE;
/*!40000 ALTER TABLE `password_reset_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `password_reset_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `priority`
--

DROP TABLE IF EXISTS `priority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `priority` (
  `id` tinyint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `sort_order` smallint NOT NULL DEFAULT '100',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `priority`
--

LOCK TABLES `priority` WRITE;
/*!40000 ALTER TABLE `priority` DISABLE KEYS */;
INSERT INTO `priority` VALUES (1,'low',40),(2,'normal',50),(3,'high',60),(4,'urgent',70);
/*!40000 ALTER TABLE `priority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relation_type`
--

DROP TABLE IF EXISTS `relation_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `relation_type` (
  `id` tinyint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `sort_order` smallint NOT NULL DEFAULT '100',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relation_type`
--

LOCK TABLES `relation_type` WRITE;
/*!40000 ALTER TABLE `relation_type` DISABLE KEYS */;
INSERT INTO `relation_type` VALUES (1,'duplicate_of',10),(2,'relates_to',20),(3,'blocked_by',30),(4,'blocks',40);
/*!40000 ALTER TABLE `relation_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sla_policy`
--

DROP TABLE IF EXISTS `sla_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sla_policy` (
  `id` char(36) NOT NULL,
  `org_id` char(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `target_first_response_minutes` int NOT NULL,
  `target_resolution_minutes` int NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_sla_org_name` (`org_id`,`name`),
  CONSTRAINT `sla_policy_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `organization` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sla_policy`
--

LOCK TABLES `sla_policy` WRITE;
/*!40000 ALTER TABLE `sla_policy` DISABLE KEYS */;
/*!40000 ALTER TABLE `sla_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` char(36) NOT NULL,
  `org_id` char(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_tag_org_name` (`org_id`,`name`),
  CONSTRAINT `tag_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `organization` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team` (
  `id` char(36) NOT NULL,
  `org_id` char(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_team_org_name` (`org_id`,`name`),
  CONSTRAINT `team_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `organization` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES ('15a8e911-075f-49ea-ac0d-2fa3e4f406a1','36eacbc4-77c9-4360-9d78-99d262ebd5be','Updated Support Team',NULL),('97bc0765-62dd-4652-87a7-58abf6a96f61','36eacbc4-77c9-4360-9d78-99d262ebd5be','Support Team',NULL);
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_member`
--

DROP TABLE IF EXISTS `team_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_member` (
  `team_id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  PRIMARY KEY (`team_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `team_member_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`) ON DELETE CASCADE,
  CONSTRAINT `team_member_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_member`
--

LOCK TABLES `team_member` WRITE;
/*!40000 ALTER TABLE `team_member` DISABLE KEYS */;
INSERT INTO `team_member` VALUES ('97bc0765-62dd-4652-87a7-58abf6a96f61','c0a80103-99b3-18ce-8199-b3d9a7a90002');
/*!40000 ALTER TABLE `team_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket` (
  `id` char(36) NOT NULL,
  `org_id` char(36) NOT NULL,
  `requester_id` char(36) NOT NULL,
  `subject` text NOT NULL,
  `body` text NOT NULL,
  `status_id` tinyint NOT NULL,
  `priority_id` tinyint NOT NULL,
  `category_id` char(36) DEFAULT NULL,
  `assigned_user_id` char(36) DEFAULT NULL,
  `assigned_team_id` char(36) DEFAULT NULL,
  `is_internal` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`),
  KEY `requester_id` (`requester_id`),
  KEY `status_id` (`status_id`),
  KEY `priority_id` (`priority_id`),
  KEY `category_id` (`category_id`),
  KEY `assigned_user_id` (`assigned_user_id`),
  KEY `assigned_team_id` (`assigned_team_id`),
  CONSTRAINT `ticket_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `organization` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ticket_ibfk_2` FOREIGN KEY (`requester_id`) REFERENCES `app_user` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `ticket_ibfk_3` FOREIGN KEY (`status_id`) REFERENCES `ticket_status` (`id`),
  CONSTRAINT `ticket_ibfk_4` FOREIGN KEY (`priority_id`) REFERENCES `priority` (`id`),
  CONSTRAINT `ticket_ibfk_5` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL,
  CONSTRAINT `ticket_ibfk_6` FOREIGN KEY (`assigned_user_id`) REFERENCES `app_user` (`id`) ON DELETE SET NULL,
  CONSTRAINT `ticket_ibfk_7` FOREIGN KEY (`assigned_team_id`) REFERENCES `team` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket` DISABLE KEYS */;
INSERT INTO `ticket` VALUES ('30f197b9-c92b-4412-b4a6-a446e31b8099','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Example Ticket','This is the body of the ticket',1,1,NULL,'c0a80103-99b3-18ce-8199-b3d9a7a90002','97bc0765-62dd-4652-87a7-58abf6a96f61',0,'2025-10-05 10:06:53','2025-10-05 10:06:53',NULL),('5cfb6675-28bc-44ad-b9ae-3313ee974d6f','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Example Ticket','This is the body of the ticket',1,1,NULL,NULL,NULL,0,'2025-10-05 09:52:42','2025-10-05 09:52:42',NULL),('6e8b82d5-0fba-43bc-9e9c-aa9f69b5f737','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Example Ticket','This is the body of the ticket',1,1,NULL,NULL,NULL,0,'2025-10-05 09:52:15','2025-10-05 09:52:15',NULL),('902c352e-3bb7-42ca-9f04-d6fc8096ca0b','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Example Ticket','This is the body of the ticket',1,1,NULL,NULL,NULL,0,'2025-10-05 09:35:16','2025-10-05 09:35:16',NULL),('9e2d3c01-c251-496b-8b9d-6f112d1b4f08','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Example Ticket','This is the body of the ticket',1,1,NULL,NULL,NULL,0,'2025-10-05 09:39:36','2025-10-05 09:39:36',NULL),('c0a80103-99b9-1099-8199-b920d5dc0000','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c0a80103-99ba-1412-8199-ba1453b10000','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c0a80103-99bd-124e-8199-bd22b14d0000','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c0a80103-99bd-124e-8199-bd243de50001','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c0a80103-99bd-13ab-8199-bd33d4ae0000','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c0a80103-99bd-150d-8199-bd25307b0000','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c0a80103-99bd-173f-8199-bd2790290000','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c0a80103-99bd-1864-8199-bd287ca60000','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c0a80103-99bd-1b13-8199-bd2b58a90000','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c0a80103-99bd-1c58-8199-bd4c71490000','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c0a80103-99bd-1d17-8199-bd2d309c0000','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c0a80103-99bd-1e30-8199-bd2e7eb00000','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c0a80103-99bd-1e30-8199-bd3042770001','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c0a80103-99bd-1e30-8199-bd3068660002','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Issue without assignment','No user or team assigned yet',1,2,NULL,NULL,NULL,0,NULL,NULL,NULL),('c18c2a7f-6745-4d41-96fa-eddfd877acd5','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Example Ticket','This is the body of the ticket',1,1,NULL,NULL,NULL,0,'2025-10-05 09:45:13','2025-10-05 09:45:13',NULL),('e0a2be5b-d8ed-4b48-8502-7f21bf484178','36eacbc4-77c9-4360-9d78-99d262ebd5be','5a31a87f-b4af-4bb5-9867-9469791ad2be','Example Ticket','This is the body of the ticket',1,1,NULL,NULL,NULL,0,'2025-10-05 09:30:58','2025-10-05 09:30:58',NULL);
/*!40000 ALTER TABLE `ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket_comment`
--

DROP TABLE IF EXISTS `ticket_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket_comment` (
  `id` char(36) NOT NULL,
  `org_id` char(36) NOT NULL,
  `ticket_id` char(36) NOT NULL,
  `author_id` char(36) NOT NULL,
  `body` text NOT NULL,
  `is_internal` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`),
  KEY `ticket_id` (`ticket_id`),
  KEY `author_id` (`author_id`),
  CONSTRAINT `ticket_comment_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `organization` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ticket_comment_ibfk_2` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ticket_comment_ibfk_3` FOREIGN KEY (`author_id`) REFERENCES `app_user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket_comment`
--

LOCK TABLES `ticket_comment` WRITE;
/*!40000 ALTER TABLE `ticket_comment` DISABLE KEYS */;
INSERT INTO `ticket_comment` VALUES ('10e72089-7107-44bf-aac2-fcbebb1a35ae','36eacbc4-77c9-4360-9d78-99d262ebd5be','30f197b9-c92b-4412-b4a6-a446e31b8099','5a31a87f-b4af-4bb5-9867-9469791ad2be','This is a test comment from 2',0,'2025-10-06 12:54:26'),('6e6432d0-5f3f-48c9-8c53-b3ae386fe81c','36eacbc4-77c9-4360-9d78-99d262ebd5be','30f197b9-c92b-4412-b4a6-a446e31b8099','5a31a87f-b4af-4bb5-9867-9469791ad2be','This is a test comment from 2',0,'2025-10-07 03:25:06'),('a4c0aac5-689c-4e2a-825a-63562314c63b','36eacbc4-77c9-4360-9d78-99d262ebd5be','30f197b9-c92b-4412-b4a6-a446e31b8099','5a31a87f-b4af-4bb5-9867-9469791ad2be','This is a test comment from 2',0,'2025-10-07 03:21:33');
/*!40000 ALTER TABLE `ticket_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket_relation`
--

DROP TABLE IF EXISTS `ticket_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket_relation` (
  `id` char(36) NOT NULL,
  `org_id` char(36) NOT NULL,
  `src_ticket_id` char(36) NOT NULL,
  `dst_ticket_id` char(36) NOT NULL,
  `relation_type_id` tinyint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ticket_relation` (`org_id`,`src_ticket_id`,`dst_ticket_id`,`relation_type_id`),
  KEY `src_ticket_id` (`src_ticket_id`),
  KEY `dst_ticket_id` (`dst_ticket_id`),
  KEY `relation_type_id` (`relation_type_id`),
  CONSTRAINT `ticket_relation_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `organization` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ticket_relation_ibfk_2` FOREIGN KEY (`src_ticket_id`) REFERENCES `ticket` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ticket_relation_ibfk_3` FOREIGN KEY (`dst_ticket_id`) REFERENCES `ticket` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ticket_relation_ibfk_4` FOREIGN KEY (`relation_type_id`) REFERENCES `relation_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket_relation`
--

LOCK TABLES `ticket_relation` WRITE;
/*!40000 ALTER TABLE `ticket_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `ticket_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket_sla`
--

DROP TABLE IF EXISTS `ticket_sla`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket_sla` (
  `ticket_id` char(36) NOT NULL,
  `sla_policy_id` char(36) NOT NULL,
  `first_response_due_at` timestamp NULL DEFAULT NULL,
  `resolution_due_at` timestamp NULL DEFAULT NULL,
  `first_response_met` tinyint(1) DEFAULT NULL,
  `resolution_met` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ticket_id`),
  KEY `sla_policy_id` (`sla_policy_id`),
  CONSTRAINT `ticket_sla_ibfk_1` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ticket_sla_ibfk_2` FOREIGN KEY (`sla_policy_id`) REFERENCES `sla_policy` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket_sla`
--

LOCK TABLES `ticket_sla` WRITE;
/*!40000 ALTER TABLE `ticket_sla` DISABLE KEYS */;
/*!40000 ALTER TABLE `ticket_sla` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket_status`
--

DROP TABLE IF EXISTS `ticket_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket_status` (
  `id` tinyint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `is_final` tinyint(1) NOT NULL DEFAULT '0',
  `sort_order` smallint NOT NULL DEFAULT '100',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket_status`
--

LOCK TABLES `ticket_status` WRITE;
/*!40000 ALTER TABLE `ticket_status` DISABLE KEYS */;
INSERT INTO `ticket_status` VALUES (1,'open',0,10),(2,'pending',0,20),(3,'on_hold',0,30),(4,'resolved',1,40),(5,'closed',1,50);
/*!40000 ALTER TABLE `ticket_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket_tag`
--

DROP TABLE IF EXISTS `ticket_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket_tag` (
  `ticket_id` char(36) NOT NULL,
  `tag_id` char(36) NOT NULL,
  PRIMARY KEY (`ticket_id`,`tag_id`),
  KEY `tag_id` (`tag_id`),
  CONSTRAINT `ticket_tag_ibfk_1` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ticket_tag_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket_tag`
--

LOCK TABLES `ticket_tag` WRITE;
/*!40000 ALTER TABLE `ticket_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `ticket_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket_watcher`
--

DROP TABLE IF EXISTS `ticket_watcher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket_watcher` (
  `ticket_id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  PRIMARY KEY (`ticket_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `ticket_watcher_ibfk_1` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ticket_watcher_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket_watcher`
--

LOCK TABLES `ticket_watcher` WRITE;
/*!40000 ALTER TABLE `ticket_watcher` DISABLE KEYS */;
/*!40000 ALTER TABLE `ticket_watcher` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-07 12:07:41
