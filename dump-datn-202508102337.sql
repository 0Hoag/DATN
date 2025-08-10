-- MySQL dump 10.13  Distrib 8.0.42, for Linux (x86_64)
--
-- Host: localhost    Database: datn
-- ------------------------------------------------------
-- Server version	8.0.42-0ubuntu0.22.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activity_logs`
--

DROP TABLE IF EXISTS `activity_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_logs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `action` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `module` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_action_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKah7if63ea12g20v62jlgkfahg` (`user_action_id`),
  CONSTRAINT `FKah7if63ea12g20v62jlgkfahg` FOREIGN KEY (`user_action_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_logs`
--

LOCK TABLES `activity_logs` WRITE;
/*!40000 ALTER TABLE `activity_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `addresses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address_line` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `is_default` bit(1) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `is_delete` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1fa36y2oqhao3wgg2rw1pi459` (`user_id`),
  CONSTRAINT `FK1fa36y2oqhao3wgg2rw1pi459` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
INSERT INTO `addresses` VALUES (1,'tphcm','2025-07-24 23:12:43.405986','tien',_binary '','0123456789','2025-07-24 23:12:43.406060',3,NULL),(2,'tphcm','2025-07-25 10:47:57.411700','tien2',NULL,'0123456789','2025-07-25 10:47:57.411721',3,NULL),(3,'tphcm','2025-07-25 10:48:23.710634','tien3',NULL,'0123456789','2025-07-25 10:48:23.710656',3,NULL),(4,'tphcm','2025-07-25 11:07:39.764236','tien3',NULL,'0123454545','2025-07-25 11:07:39.764288',4,NULL),(5,'hue','2025-07-25 11:07:58.920838','tien 3 ',NULL,'0123454545','2025-07-25 11:07:58.920847',4,NULL),(6,'tphcm , tan binh','2025-07-25 18:14:33.164015','tien',_binary '\0','0123456789','2025-07-25 18:14:33.164044',4,NULL),(7,'tphcm','2025-07-25 18:14:57.414802','tien',_binary '\0','0123456789','2025-07-25 18:14:57.414825',4,NULL),(8,'tphcm','2025-07-25 19:00:27.978675','tien',_binary '\0','0123456789','2025-07-25 19:00:27.978736',4,_binary ''),(11,'tphcm','2025-07-29 21:09:27.936449','tien',_binary '','0123456789','2025-07-29 21:09:27.936466',4,_binary '\0'),(15,'tphcm','2025-08-09 04:09:30.087456','tien',NULL,'0123456789','2025-08-09 04:09:30.087499',7,NULL),(19,'tphcm','2025-08-10 10:08:34.431202','tien',_binary '','0123456789','2025-08-10 10:08:34.431232',9,_binary '\0');
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `price` decimal(10,2) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `cart_id` int DEFAULT NULL,
  `product_variant_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpcttvuq4mxppo8sxggjtn5i2c` (`cart_id`),
  KEY `FKn1s4l7h0vm4o259wpu7ft0y2y` (`product_variant_id`),
  CONSTRAINT `FKn1s4l7h0vm4o259wpu7ft0y2y` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`),
  CONSTRAINT `FKpcttvuq4mxppo8sxggjtn5i2c` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
INSERT INTO `cart_items` VALUES (1,1000000.00,3,1,1),(2,1000000.00,1,2,1),(3,1000000.00,1,3,1),(4,25000000.00,1,4,2),(5,25000000.00,1,5,2),(14,NULL,1,8,1),(15,NULL,1,8,2),(57,1000000.00,1,20,1),(58,NULL,1,12,3),(59,NULL,0,12,2),(60,NULL,3,12,1),(63,25000000.00,1,22,2),(105,10000000.00,1,25,4),(126,3000000.00,1,21,3),(128,1000000.00,5,21,1),(137,100000.00,2,28,14),(138,1000000.00,1,28,16);
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` date DEFAULT NULL,
  `session_id` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb5o626f86h46m4s7ms6ginnop` (`user_id`),
  CONSTRAINT `FKb5o626f86h46m4s7ms6ginnop` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
INSERT INTO `carts` VALUES (1,'2025-07-24','579B124954A435969526EC23326F916E',NULL),(2,'2025-07-24','83A08A28D7A6B9260D208EB7F7087D28',NULL),(3,'2025-07-24','3DEDBD942CE1A061FAC3B7E9C78201BD',NULL),(4,'2025-07-24','74BDF3B4B5B1E587C8027BD72F12A59B',NULL),(5,'2025-07-24','73419799D7AFDA156E71C850F5EA04E7',NULL),(8,'2025-07-24',NULL,1),(11,'2025-07-24','8BA5972279A9BB951E0768AF1EE1E1C4',NULL),(12,'2025-07-24',NULL,3),(19,'2025-07-25','DC7FADE98D064FE7DF371EE325D17B4B',NULL),(20,'2025-07-25','3DE027B1BC30F9BE39742D7069CE8077',NULL),(21,'2025-07-25',NULL,4),(22,'2025-07-25','56C543670D2F05898BB8B0AE58D44567',NULL),(23,'2025-07-25','816EF78C0375567180B528952BFE162F',NULL),(24,'2025-07-25',NULL,5),(25,'2025-08-04','47CECE4C4854EABE8244DD1F6C5C5CC5',NULL),(26,'2025-08-10','87483CDD4C0E4DE51F1E4E82996B528D',NULL),(27,'2025-08-10',NULL,9),(28,'2025-08-10','27407470D54853255E55EA28891BF5C9',NULL);
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text,
  `is_show` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `parent_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_t8o6pivur7nn124jehx7cygw5` (`name`),
  UNIQUE KEY `UK_oul14ho7bctbefv8jywp5v3i2` (`slug`),
  KEY `FKsaok720gsu4u2wrgbk10b5n8d` (`parent_id`),
  CONSTRAINT `FKsaok720gsu4u2wrgbk10b5n8d` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'2025-07-24 11:12:32.099611','',_binary '','Điện thoại','dien-thoai','2025-08-10 21:23:23.727482',NULL),(2,'2025-07-24 11:12:40.421518','',_binary '','Laptop','laptop','2025-07-24 11:25:02.499291',NULL),(4,'2025-07-24 12:04:17.981150','',_binary '','Đồng hồ','dong-ho','2025-07-24 12:04:21.655298',NULL),(9,'2025-08-09 04:27:59.769551','',_binary '','Điện thoại 2','dien-thoai-2','2025-08-09 14:42:34.158975',NULL),(10,'2025-08-09 13:57:34.051836','',_binary '','test sub','test-sub','2025-08-09 14:28:51.005406',9),(11,'2025-08-09 14:17:35.924052','',_binary '','test sub 1','test-sub-1','2025-08-09 14:28:55.137448',9),(12,'2025-08-09 14:18:12.917510','',_binary '','test sub 2','test-sub-2','2025-08-09 14:28:59.519837',9),(13,'2025-08-09 14:48:40.892127','',_binary '\0','Danh mục 2','danh-muc-2','2025-08-09 15:35:51.353424',NULL);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forgot_password`
--

DROP TABLE IF EXISTS `forgot_password`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `forgot_password` (
  `fp_id` int NOT NULL AUTO_INCREMENT,
  `expiration_time` datetime(6) NOT NULL,
  `otp` int NOT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`fp_id`),
  UNIQUE KEY `UK_ss96nm4ed1jmllpxib14p1r7v` (`user_id`),
  CONSTRAINT `FKjfa13lhndn1q66kheuyjk2i5l` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forgot_password`
--

LOCK TABLES `forgot_password` WRITE;
/*!40000 ALTER TABLE `forgot_password` DISABLE KEYS */;
INSERT INTO `forgot_password` VALUES (7,'2025-08-05 02:35:01.709000',577169,6),(8,'2025-08-09 05:39:14.508000',708703,2);
/*!40000 ALTER TABLE `forgot_password` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invalidated_token`
--

DROP TABLE IF EXISTS `invalidated_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invalidated_token` (
  `id` varchar(255) NOT NULL,
  `exprity_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invalidated_token`
--

LOCK TABLES `invalidated_token` WRITE;
/*!40000 ALTER TABLE `invalidated_token` DISABLE KEYS */;
INSERT INTO `invalidated_token` VALUES ('02884360-9c0f-4ca9-85e1-0af542b86579','2025-07-25 20:39:54.000000'),('03d43f62-f3f3-4209-8777-925a081ad0b3','2025-07-25 13:26:03.000000'),('03ea946e-4836-4111-8f9f-4a7abcd687b3','2025-07-25 19:53:20.000000'),('0509529c-6a1a-4fd0-924b-28095a68914d','2025-07-25 13:21:21.000000'),('0521bf98-762c-4ab0-bcab-61408480c516','2025-07-31 17:55:01.000000'),('0cf58ad0-2220-4b1b-9142-f539576fce5a','2025-07-31 21:21:25.000000'),('120e1856-6cf6-426a-826b-fd6f01f3eaf8','2025-08-09 15:22:02.000000'),('150d25d9-7ae3-4753-9095-82fbdcbf4d49','2025-08-09 06:05:03.000000'),('170446a0-46eb-4526-9ce7-5177fd760068','2025-07-25 22:38:41.000000'),('1805bba8-4451-46d7-9945-f5a29e0b0dce','2025-07-25 21:08:53.000000'),('19d3362c-b825-4bf4-8df0-171b066d8cc7','2025-07-31 20:46:45.000000'),('1d2d90d6-0f6b-49e0-86f2-f50dce6bc7d4','2025-07-25 20:52:04.000000'),('1e42a0c7-777c-44e6-8c07-10ab143f9c8d','2025-07-25 20:24:07.000000'),('25bef236-bc04-443b-8ef1-5b1a53e9c11d','2025-08-07 23:37:18.000000'),('27ed5bf8-9b0f-4064-82f3-30d6b6588f6f','2025-07-25 21:13:18.000000'),('29e257f1-85eb-426e-bb5c-8ae6e4334630','2025-07-25 13:14:36.000000'),('2c08d110-92cf-4888-80bd-ad4b73eb280d','2025-07-25 14:02:23.000000'),('31e2e8e8-418f-406c-8799-ea00207614e6','2025-07-25 13:35:20.000000'),('336f923a-fb48-4244-a69a-a643371fe8b0','2025-07-25 22:08:53.000000'),('39da8e7e-9e0a-40de-8927-e0b55f473803','2025-07-31 21:22:42.000000'),('3a3a64cd-94c8-46d0-aba8-f21b64947833','2025-08-10 14:45:11.000000'),('3a78c276-ba70-4fb3-9f54-d5cab1072af6','2025-07-30 00:01:33.000000'),('3a9e8a16-ed08-40a4-ad52-4fd82edbeda3','2025-07-25 22:41:42.000000'),('3b593c15-59bd-47bb-ad3d-1428cddc8536','2025-07-25 22:04:12.000000'),('3ccddd26-4b4d-48bb-a484-db8652c72b42','2025-08-09 05:58:10.000000'),('3e3f0a39-922b-4b58-af24-3e82f3982029','2025-07-31 21:39:38.000000'),('3edfa8a5-16a9-471e-92bf-0c1c0bc86d62','2025-08-05 04:00:27.000000'),('3ff10db8-cb04-4dce-8aa0-052c4d23b35b','2025-08-04 15:12:13.000000'),('40e32be2-ed93-4604-ae3e-8947159bfb42','2025-08-10 11:31:45.000000'),('4374ea15-e19b-4dcb-82cf-578b4247ba8c','2025-07-25 20:11:55.000000'),('4461edb4-3dd8-45ac-916c-9482af1fa84d','2025-08-08 17:26:43.000000'),('4820a2f3-5a43-4e7e-b923-a44900e1b9ed','2025-08-10 23:45:29.000000'),('4e0f96cf-f045-4228-9019-3f18e27db236','2025-07-25 21:40:02.000000'),('4e3ba2c1-e427-4228-aa23-39f45c8efb27','2025-08-05 04:14:34.000000'),('4eebc67d-7da0-4d16-abc2-8a2e59d37d9f','2025-07-25 21:34:51.000000'),('50bd3435-6b88-4011-b6aa-8b366820d04f','2025-07-25 21:09:38.000000'),('50bf9204-cdef-48f3-aa48-7d8fa5130a45','2025-07-31 21:14:13.000000'),('543f315e-f1ea-4eff-a701-2719fa125297','2025-07-25 18:42:47.000000'),('5600c0d0-d278-4fd6-8551-9eb745e25b89','2025-07-29 13:28:56.000000'),('58e12fa5-b975-48a1-b14b-655ffd3ffb1c','2025-07-25 13:27:12.000000'),('609fce20-e62d-4d11-8a3a-256f950394b3','2025-07-31 21:47:14.000000'),('6222703b-8d25-4f7e-be7c-beb1e2f6a882','2025-08-10 11:25:38.000000'),('6bda5a72-77de-4e73-9443-bcce60e6ba82','2025-07-25 20:40:06.000000'),('6c4934f1-9e58-42f3-9c7b-62a052502b8c','2025-08-05 03:57:52.000000'),('70423e7d-a427-4e19-94a8-906233582318','2025-07-25 22:26:18.000000'),('7181d48c-be70-4b0a-9ba7-3249d1cc6008','2025-08-08 17:07:27.000000'),('7296378a-9bc0-4718-95b4-ad2687c7e17b','2025-07-25 20:58:52.000000'),('7316ca87-e3bf-48f5-9c79-954b52a5c2a1','2025-07-25 13:36:04.000000'),('76a75959-5ac5-4ee0-b929-530b87be89c9','2025-07-31 21:34:40.000000'),('7a4efac1-8c83-4020-a7d1-6639812d582f','2025-08-05 04:02:58.000000'),('7f17d8b0-8d04-4778-9762-dbf16d5ac632','2025-08-09 15:16:34.000000'),('80142a7d-a771-4f85-a417-b03673b5c858','2025-07-31 21:13:17.000000'),('86170872-274f-4828-b0df-62f82d58b34a','2025-08-10 23:03:57.000000'),('87845d07-d772-4b6c-addc-2d40c5fd5a4f','2025-07-25 13:31:55.000000'),('8ad5e739-0395-472d-be9f-8044cf2f703c','2025-07-25 17:47:34.000000'),('8c7c4a28-3371-4b83-ae39-ca6c7efdde19','2025-07-31 21:41:09.000000'),('8e949857-c1ac-4cd2-b7d2-9dd7bbc5e8ec','2025-08-09 07:18:45.000000'),('8fe7993b-82a6-4e44-9cd3-dec66f362ed1','2025-07-31 21:42:38.000000'),('97c1b283-b9cf-40b0-8aa1-82b07c47e954','2025-08-09 15:16:12.000000'),('9814757b-330a-486f-9363-221b69e413e5','2025-08-10 11:27:19.000000'),('9aa41e5a-9bc2-4833-bec7-6e8c9224140c','2025-08-10 11:57:01.000000'),('9facaef1-6ccd-4a9e-a37f-0ef5414cc2b9','2025-07-28 19:37:32.000000'),('a07b2c9c-6603-498a-9fa2-3dec9feaa974','2025-07-25 21:27:13.000000'),('a64011d1-d7bb-4e5c-8e0b-ce1746ec971d','2025-08-08 17:03:26.000000'),('a7c0931e-2fa1-4ec1-916f-2115b130f585','2025-08-05 04:00:14.000000'),('ac26da86-a1f8-4a17-a5a3-4a828b8d738c','2025-08-10 11:29:59.000000'),('ae8f0981-f854-4043-b8e4-76f31d45a80f','2025-07-25 18:28:56.000000'),('b486d093-0bfb-4407-96d8-489ab63d0026','2025-08-10 11:57:22.000000'),('baa325f9-ee9c-4b4c-b52d-5cbede828e75','2025-08-05 04:15:35.000000'),('bbe80aed-8f6b-4cb4-b866-576988440cc4','2025-08-09 22:42:31.000000'),('bf41b66b-bfd6-4cf4-9aa1-b0d379973275','2025-08-09 05:31:35.000000'),('cdd595de-6de5-4733-9249-017e2246888a','2025-07-25 22:05:40.000000'),('cfb99790-362b-4839-ad30-95be1c201348','2025-08-09 15:33:03.000000'),('d3d1df64-79cd-4161-bdbf-b58a43ca9f6d','2025-07-31 21:35:38.000000'),('d6e49c20-ba5d-4237-a33d-7836dda28d22','2025-07-25 17:45:58.000000'),('d87674cb-ac72-48ea-9d65-5bd0eec5b546','2025-08-10 14:50:21.000000'),('d8978b0d-16fd-4b45-92d0-2a903648f043','2025-07-31 21:42:57.000000'),('d9c4f5c6-2992-428a-bc33-48a6be738e2e','2025-08-08 17:12:35.000000'),('daba8ed6-c664-4ffe-b0ff-4c55a5b2422f','2025-07-25 14:26:32.000000'),('e56ba49e-bc77-465a-bd9e-a2bef1c0f6c3','2025-07-25 13:34:39.000000'),('ef82b34c-ceb5-4615-b725-5c83616269e6','2025-07-29 13:24:05.000000'),('f4f3cf7f-36df-4c25-b0e6-57f2e770e8fb','2025-07-31 21:48:12.000000'),('f792319f-cdca-4824-886f-fb801cba5103','2025-07-25 13:21:43.000000'),('f9047679-5b75-4777-920b-7b21d15c4d62','2025-07-25 13:05:16.000000'),('fdf6967c-dc8b-4af6-8c09-0d95a637802b','2025-08-05 04:15:05.000000');
/*!40000 ALTER TABLE `invalidated_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `order_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `product_variant_id` int DEFAULT NULL,
  `is_reviewed` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjyu2qbqt8gnvno9oe9j2s2ldk` (`order_id`),
  KEY `FK4q98utpd73imf4yhttm3w0eax` (`product_id`),
  KEY `FKsx0enyl805emarbr0bgoln1oq` (`product_variant_id`),
  CONSTRAINT `FK4q98utpd73imf4yhttm3w0eax` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKjyu2qbqt8gnvno9oe9j2s2ldk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKsx0enyl805emarbr0bgoln1oq` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
INSERT INTO `order_details` VALUES (1,'2025-07-24 23:12:43.415271',NULL,3000000.00,1,1,3,3,NULL),(2,'2025-07-25 13:26:51.707692',NULL,1000000.00,1,2,1,1,NULL),(3,'2025-07-25 14:39:35.399708',NULL,25000000.00,2,3,2,2,NULL),(4,'2025-07-25 14:39:35.400164',NULL,3000000.00,5,3,3,3,NULL),(5,'2025-07-25 14:55:44.886102',NULL,1000000.00,1,4,5,6,NULL),(6,'2025-07-25 14:57:07.858178',NULL,1000000.00,1,5,1,1,NULL),(7,'2025-07-25 14:58:26.151630',NULL,1000000.00,1,6,1,1,NULL),(8,'2025-07-25 15:01:42.943423',NULL,3000000.00,1,7,3,3,_binary ''),(9,'2025-07-25 15:37:37.746153',NULL,1000000.00,1,8,1,1,NULL),(10,'2025-07-25 15:41:09.280549',NULL,1000000.00,1,9,1,1,NULL),(11,'2025-07-25 15:45:24.527909',NULL,1000000.00,1,10,1,1,NULL),(12,'2025-07-25 15:48:14.233202',NULL,1000000.00,1,11,1,1,NULL),(13,'2025-07-25 15:48:40.443388',NULL,1000000.00,1,12,1,1,NULL),(14,'2025-07-25 15:55:52.039247',NULL,10000000.00,1,13,4,4,NULL),(15,'2025-07-25 21:04:12.256628',NULL,1000000.00,1,14,1,1,NULL),(16,'2025-07-25 21:04:12.259423',NULL,1000000.00,1,14,5,13,NULL),(17,'2025-07-25 21:09:11.931613',NULL,1000000.00,1,15,1,1,_binary ''),(19,'2025-07-25 21:36:44.415782',NULL,3000000.00,2,17,3,3,NULL),(21,'2025-07-25 21:51:19.223419',NULL,1000000.00,1,19,1,1,_binary ''),(22,'2025-08-05 02:01:14.196729',NULL,1000000.00,1,20,1,1,_binary '\0'),(23,'2025-08-05 02:03:39.164930',NULL,3000000.00,1,21,3,3,_binary '\0'),(24,'2025-08-05 02:05:28.560140',NULL,3000000.00,1,22,3,3,_binary '\0'),(26,'2025-08-05 02:10:39.421869',NULL,1000000.00,1,24,5,6,_binary '\0'),(27,'2025-08-05 02:13:27.265469',NULL,3000000.00,1,25,3,3,_binary '\0'),(28,'2025-08-05 02:13:39.125071',NULL,3000000.00,1,26,3,3,_binary '\0'),(29,'2025-08-07 17:14:37.138087',NULL,3000000.00,1,27,3,3,_binary '\0'),(30,'2025-08-07 17:19:56.359965',NULL,1000000.00,1,28,1,1,_binary '\0'),(31,'2025-08-07 17:24:09.851543',NULL,1000000.00,1,29,1,1,_binary '\0'),(32,'2025-08-08 16:15:54.587003',NULL,1000000.00,1,30,1,1,_binary '\0'),(33,'2025-08-08 16:15:54.587185',NULL,10000000.00,1,30,4,4,_binary '\0'),(34,'2025-08-09 04:09:30.098996',NULL,1000000.00,1,31,1,1,_binary '\0'),(35,'2025-08-09 12:34:11.852980',NULL,1000000.00,1,32,1,1,_binary '\0'),(36,'2025-08-09 12:36:33.345403',NULL,3000000.00,1,33,3,3,_binary '\0'),(37,'2025-08-09 12:36:48.198267',NULL,3000000.00,1,34,3,3,_binary '\0'),(38,'2025-08-09 15:11:29.741255',NULL,1000000.00,1,35,1,1,_binary ''),(39,'2025-08-09 15:11:52.059379',NULL,1000000.00,1,36,1,1,_binary ''),(40,'2025-08-09 21:44:57.180306',NULL,1000000.00,1,37,1,1,_binary '\0'),(41,'2025-08-10 10:09:08.802707',NULL,1000000.00,1,38,1,1,_binary '\0'),(42,'2025-08-10 10:14:37.223859',NULL,1000000.00,1,39,1,1,_binary '\0');
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_returns`
--

DROP TABLE IF EXISTS `order_returns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_returns` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `payment_method` int DEFAULT NULL,
  `reason` text,
  `refund_amount` decimal(19,2) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `order_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKaifre9nq9q3r1lng6p4pxvoxk` (`order_id`),
  KEY `FKd70mrm1uv35qvmuuq2dpqqjuo` (`user_id`),
  CONSTRAINT `FKaifre9nq9q3r1lng6p4pxvoxk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKd70mrm1uv35qvmuuq2dpqqjuo` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_returns`
--

LOCK TABLES `order_returns` WRITE;
/*!40000 ALTER TABLE `order_returns` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_returns` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `is_delete` bit(1) DEFAULT NULL,
  `is_return` bit(1) DEFAULT NULL,
  `note` text,
  `order_status` varchar(20) DEFAULT NULL,
  `payment_status` varchar(255) DEFAULT NULL,
  `total_amount` decimal(19,2) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address_id` int DEFAULT NULL,
  `payment_method` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `voucher_id` int DEFAULT NULL,
  `reason` text,
  PRIMARY KEY (`id`),
  KEY `FKhlglkvf5i60dv6dn397ethgpt` (`address_id`),
  KEY `FK4q9i5ocee718mqyc415foi9ct` (`payment_method`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  KEY `FKdimvsocblb17f45ikjr6xn1wj` (`voucher_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK4q9i5ocee718mqyc415foi9ct` FOREIGN KEY (`payment_method`) REFERENCES `payment_methods` (`id`),
  CONSTRAINT `FKdimvsocblb17f45ikjr6xn1wj` FOREIGN KEY (`voucher_id`) REFERENCES `vouchers` (`id`),
  CONSTRAINT `FKhlglkvf5i60dv6dn397ethgpt` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2025-07-24 23:12:43.411654',_binary '\0',_binary '\0','','CONFIRMED','PENDING',3000000.00,'2025-07-29 22:21:45.754872',1,1,3,NULL,NULL),(2,'2025-07-25 13:26:51.704816',_binary '\0',_binary '\0','','PENDING','PENDING',1000000.00,'2025-07-25 13:26:51.704830',4,1,4,NULL,NULL),(3,'2025-07-25 14:39:35.397227',_binary '\0',_binary '\0','','PENDING','PENDING',65000000.00,'2025-07-25 14:39:35.397379',4,1,4,NULL,NULL),(4,'2025-07-25 14:55:44.885018',_binary '\0',_binary '\0','','PENDING','PENDING',1000000.00,'2025-07-25 14:55:44.885084',4,2,4,NULL,NULL),(5,'2025-07-25 14:57:07.857417',_binary '\0',_binary '\0','','PENDING','PENDING',1000000.00,'2025-07-25 14:57:07.857454',4,2,4,NULL,NULL),(6,'2025-07-25 14:58:26.151236',_binary '\0',_binary '\0','','PENDING','PENDING',1000000.00,'2025-07-25 14:58:26.151255',4,1,4,NULL,NULL),(7,'2025-07-25 15:01:42.942981',_binary '\0',_binary '\0','','RECEIED','PAID',3000000.00,'2025-07-25 15:17:13.479436',4,2,4,NULL,NULL),(8,'2025-07-25 15:37:37.745458',_binary '\0',_binary '\0','','PENDING','PENDING',1000000.00,'2025-07-25 15:37:37.745497',4,2,4,NULL,NULL),(9,'2025-07-25 15:41:09.280244',_binary '\0',_binary '\0','','PENDING','PENDING',1000000.00,'2025-07-25 15:41:09.280266',4,2,4,NULL,NULL),(10,'2025-07-25 15:45:24.526967',_binary '\0',_binary '\0','','PENDING','PENDING',1000000.00,'2025-07-25 15:45:24.526986',4,2,4,NULL,NULL),(11,'2025-07-25 15:48:14.232617',_binary '\0',_binary '\0','','PENDING','PENDING',1000000.00,'2025-07-25 15:48:14.232640',4,1,4,NULL,NULL),(12,'2025-07-25 15:48:40.442760',_binary '\0',_binary '\0','','PENDING','PENDING',1000000.00,'2025-07-25 15:48:40.442785',4,2,4,NULL,NULL),(13,'2025-07-25 15:55:52.038810',_binary '\0',_binary '\0','','CONFIRMED','PENDING',10000000.00,'2025-07-25 16:49:29.365612',4,2,4,NULL,NULL),(14,'2025-07-25 21:04:12.247634',_binary '\0',_binary '\0','','CONFIRMED','PENDING',2000000.00,'2025-07-25 21:04:12.247736',1,1,3,NULL,NULL),(15,'2025-07-25 21:09:11.930336',_binary '\0',_binary '\0','','RECEIED','PAID',1000000.00,'2025-07-31 20:13:26.579099',8,2,4,NULL,NULL),(16,'2025-07-25 21:25:13.278767',_binary '\0',_binary '\0','','CANCELLED','PENDING',1000000.00,'2025-07-25 21:25:13.278787',8,2,4,NULL,NULL),(17,'2025-07-25 21:36:44.415450',_binary '\0',_binary '\0','','PENDING','PENDING',6000000.00,'2025-07-25 21:36:44.415466',8,2,4,NULL,NULL),(18,'2025-07-25 21:42:34.589514',_binary '\0',_binary '\0','','CANCELLED','PENDING',3000000.00,'2025-07-25 21:42:34.589531',8,2,4,NULL,NULL),(19,'2025-07-25 21:51:19.222667',_binary '\0',_binary '\0','','RECEIED','PAID',1000000.00,'2025-08-01 21:42:08.710007',8,2,4,NULL,NULL),(20,'2025-08-05 02:01:14.195931',_binary '\0',_binary '\0','','PENDING','PENDING',990000.00,'2025-08-05 02:01:14.195950',11,1,4,1,NULL),(21,'2025-08-05 02:03:39.164374',_binary '\0',_binary '\0','','PENDING','PENDING',2970000.00,'2025-08-05 02:03:39.164386',11,1,4,1,NULL),(22,'2025-08-05 02:05:28.559652',_binary '\0',_binary '\0','','CANCELLED','PENDING',3000000.00,'2025-08-05 02:05:28.559673',11,1,4,NULL,'test'),(23,'2025-08-05 02:05:56.782173',_binary '\0',_binary '\0','','RECEIED','PAID',3000000.00,'2025-08-09 15:22:06.440176',11,1,4,NULL,NULL),(24,'2025-08-05 02:10:39.421096',_binary '\0',_binary '\0','','CANCELLED','PENDING',1000000.00,'2025-08-05 02:10:39.421130',11,2,4,NULL,'test'),(25,'2025-08-05 02:13:27.264481',_binary '\0',_binary '\0','','CANCELLED','PENDING',3000000.00,'2025-08-05 02:13:27.264503',11,1,4,NULL,'Khác'),(26,'2025-08-05 02:13:39.124854',_binary '\0',_binary '\0','','CANCELLED','PENDING',3000000.00,'2025-08-05 02:13:39.124868',11,2,4,NULL,'Khác'),(27,'2025-08-07 17:14:37.137519',_binary '\0',_binary '\0','','CANCELLED','PENDING',3000000.00,'2025-08-07 17:14:37.137532',11,2,4,NULL,'Khác'),(28,'2025-08-07 17:19:56.358969',_binary '\0',_binary '\0','','CANCELLED','PENDING',1000000.00,'2025-08-07 17:19:56.358983',11,2,4,NULL,'Khác'),(29,'2025-08-07 17:24:09.851281',_binary '\0',_binary '\0','','CANCELLED','REFUNDED',1000000.00,'2025-08-07 17:25:38.677937',11,2,4,NULL,'khac'),(30,'2025-08-08 16:15:54.586289',_binary '\0',_binary '\0','','CANCELLED','REFUNDED',10890000.00,'2025-08-08 16:16:35.816129',11,2,4,1,'Khác'),(31,'2025-08-09 04:09:30.094194',_binary '\0',_binary '\0','','CANCELLED','PENDING',1000000.00,'2025-08-09 04:09:30.094242',15,1,7,NULL,'test'),(32,'2025-08-09 12:34:11.852357',_binary '\0',_binary '\0','','PENDING','PENDING',990000.00,'2025-08-09 12:34:11.852368',11,1,4,1,NULL),(33,'2025-08-09 12:36:33.345088',_binary '\0',_binary '\0','','PENDING','PENDING',3000000.00,'2025-08-09 12:36:33.345103',11,1,4,NULL,NULL),(34,'2025-08-09 12:36:48.197916',_binary '\0',_binary '\0','','PENDING','PENDING',3000000.00,'2025-08-09 12:36:48.197945',11,1,4,NULL,NULL),(35,'2025-08-09 15:11:29.740595',_binary '\0',_binary '\0','','RECEIED','PAID',1000000.00,'2025-08-10 22:10:42.393605',11,1,4,NULL,NULL),(36,'2025-08-09 15:11:52.057944',_binary '\0',_binary '\0','','RECEIED','PAID',990000.00,'2025-08-09 21:42:42.564245',11,1,4,1,NULL),(37,'2025-08-09 21:44:57.179552',_binary '\0',_binary '\0','','CANCELLED','PENDING',990000.00,'2025-08-09 21:44:57.179574',11,1,4,3,'Khác'),(38,'2025-08-10 10:09:08.801400',_binary '\0',_binary '\0','','PENDING','PENDING',1000000.00,'2025-08-10 10:09:08.801418',19,1,9,NULL,NULL),(39,'2025-08-10 10:14:37.222866',_binary '\0',_binary '\0','','PENDING','PENDING',990000.00,'2025-08-10 10:14:37.222953',19,1,9,4,NULL);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_methods`
--

DROP TABLE IF EXISTS `payment_methods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_methods` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_methods`
--

LOCK TABLES `payment_methods` WRITE;
/*!40000 ALTER TABLE `payment_methods` DISABLE KEYS */;
INSERT INTO `payment_methods` VALUES (1,'Thanh toán tiền mặt','COD'),(2,'Thanh toán online với VN Pay','VNPAY');
/*!40000 ALTER TABLE `payment_methods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `name` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES ('ASSIGN_ROLE',NULL,'Cấp / thu hồi quyền cho người dùng khác',NULL,NULL),('ASSIGN_VOUCHER',NULL,'Gán voucher cho người dùng',NULL,NULL),('BUY_PRODUCT',NULL,'Thêm vào giỏ hàng, thanh toán',NULL,NULL),('CLAIM_VOUCHER',NULL,'Cho phép người dùng nhận voucher',NULL,NULL),('CREATE_REVIEW',NULL,'Viết đánh giá sản phẩm',NULL,NULL),('DELETE_USER_VOUCHER',NULL,'Cho phép xóa voucher của người dùng',NULL,NULL),('MANAGE_ORDERS',NULL,'Xem và cập nhật trạng thái đơn hàng toàn hệ thống',NULL,NULL),('MANAGE_PRODUCTS',NULL,'Quản lý toàn bộ sản phẩm trong hệ thống',NULL,NULL),('MANAGE_USERS',NULL,'Xem, chỉnh sửa, khóa tài khoản người dùng',NULL,NULL),('REPLY_PRODUCT_REVIEW',NULL,'Cho phép phản hồi đánh giá sản phẩm',NULL,NULL),('SYSTEM_BACKUP',NULL,'Sao lưu / phục hồi dữ liệu hệ thống',NULL,NULL),('SYSTEM_SETTINGS',NULL,'Thay đổi cấu hình hệ thống (email, tích hợp, bảo mật...)',NULL,NULL),('TRACK_ORDER',NULL,'Theo dõi đơn hàng đã đặt',NULL,NULL),('VIEW_ORDER',NULL,'Xem đơn hàng',NULL,NULL),('VIEW_PRODUCT',NULL,'Xem danh sách & chi tiết sản phẩm',NULL,NULL),('VIEW_USER_VOUCHER',NULL,'Cho phép xem các voucher của người dùng',NULL,NULL);
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_images`
--

DROP TABLE IF EXISTS `product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_images` (
  `id` int NOT NULL AUTO_INCREMENT,
  `alt_text` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `is_thumbnail` bit(1) DEFAULT NULL,
  `sort_order` int DEFAULT NULL,
  `spec_description` text,
  `updated_at` datetime(6) DEFAULT NULL,
  `product_variant_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7duksvvesmxhelrq7ugqkaq8b` (`product_variant_id`),
  CONSTRAINT `FK7duksvvesmxhelrq7ugqkaq8b` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_images`
--

LOCK TABLES `product_images` WRITE;
/*!40000 ALTER TABLE `product_images` DISABLE KEYS */;
INSERT INTO `product_images` VALUES (1,'ip11tim.jpg','2025-07-24 11:16:23.912491','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280620/mjffyczgeupqkos2iczf.jpg',_binary '',1,'ip11tim.jpg','2025-08-10 15:09:10.578650',1),(2,'41918_laptop_asus_vivobook_15_x1502za_bq127w_1_.jpg','2025-07-24 12:34:29.569176','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289309/dhg0p9o0w5qj73iwejuq.jpg',_binary '',1,'41918_laptop_asus_vivobook_15_x1502za_bq127w_1_.jpg','2025-08-10 16:52:23.512853',2),(3,'edifice_casio_ca_efr_526l_7avudfjpg_1628060035-1725366677.jpg','2025-07-24 12:36:04.232053','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289654/yyrcicninzo5lt4bartn.jpg',_binary '',1,'edifice_casio_ca_efr_526l_7avudfjpg_1628060035-1725366677.jpg','2025-08-10 16:52:30.356283',3),(4,'2023_9_13_638302015853094423_iPhone_15_Yellow_Pure_Back_iPhone_15_Yellow_Pure_Front_2up_Screen__USEN.webp','2025-07-25 12:47:57.501325','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280586/mvyemobmkmxsuo6w2kdi.webp',_binary '',1,'2023_9_13_638302015853094423_iPhone_15_Yellow_Pure_Back_iPhone_15_Yellow_Pure_Front_2up_Screen__USEN.webp','2025-08-10 16:52:40.766418',4),(5,'66855595.jpeg','2025-07-25 12:47:57.501356','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280588/tdmwwhzbjmpxtmlmfncu.jpg',_binary '\0',2,'66855595.jpeg','2025-08-10 16:52:40.766885',4),(6,'555248352.jpeg','2025-07-25 12:47:57.501359','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280598/uio9ytve3opqy6xzdmjw.jpg',_binary '\0',3,'555248352.jpeg','2025-08-10 16:52:40.767434',4),(7,'872124681.jpeg','2025-07-25 12:47:57.501363','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280601/ab0ufhbmhsq0qrmwpdcz.jpg',_binary '\0',4,'872124681.jpeg','2025-08-10 16:52:40.767827',4),(8,'ip6vang.jpg','2025-07-25 12:47:57.501365','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280605/cfk4znr9jfcmggevuzn4.jpg',_binary '\0',5,'ip6vang.jpg','2025-08-10 16:52:40.768369',4),(9,'ip6bac.webp','2025-07-25 12:47:57.501374','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280603/ueumyvznuooz6asmq4t4.webp',_binary '\0',6,'ip6bac.webp','2025-08-10 16:52:40.768755',4),(10,'ip12pro.jpg','2025-07-25 12:47:57.527313','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280659/shoflqdngfctlxtbrbr8.jpg',_binary '',1,'ip12pro.jpg','2025-07-25 12:47:57.527329',5),(11,'ip12proden.jpg','2025-07-25 12:47:57.527336','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280661/wjc3iqmnq3svjffv9kvr.jpg',_binary '\0',2,'ip12proden.jpg','2025-07-25 12:47:57.527340',5),(12,'ip12trang.jpg','2025-07-25 12:47:57.527345','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280662/w5ezjzieyfn8yrcaylfg.webp',_binary '\0',3,'ip12trang.jpg','2025-07-25 12:47:57.527349',5),(13,'ip12trang.png','2025-07-25 12:47:57.527353','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280665/xdi5a5v0rys2uhcxlnq9.png',_binary '\0',4,'ip12trang.png','2025-07-25 12:47:57.527358',5),(14,'ip12xanh.png','2025-07-25 12:47:57.527364','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280667/wucte93ngxu0ouxecfdd.png',_binary '\0',5,'ip12xanh.png','2025-07-25 12:47:57.527367',5),(15,'ip13.jpg','2025-07-25 12:47:57.527371','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280669/tuxros9sq8lyxlloeeh5.jpg',_binary '\0',6,'ip13.jpg','2025-07-25 12:47:57.527375',5),(16,'ip15pro.jpg','2025-07-25 12:47:57.527379','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280682/ho1djda8xvaxawlly0am.jpg',_binary '\0',7,'ip15pro.jpg','2025-07-25 12:47:57.527382',5),(17,'ip14protrang.jpg','2025-07-25 12:47:57.527386','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280680/qm6dhwacop1e6q73lexv.jpg',_binary '\0',8,'ip14protrang.jpg','2025-07-25 12:47:57.527390',5),(18,'ip13xanh.jpg','2025-07-25 12:47:57.527394','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280678/hwjpdd2fbil4gasqwhlt.jpg',_binary '\0',9,'ip13xanh.jpg','2025-07-25 12:47:57.527397',5),(19,'ip13promax.png','2025-07-25 12:47:57.527401','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280676/asm4qvrsbalpjebm4eay.png',_binary '\0',10,'ip13promax.png','2025-07-25 12:47:57.527404',5),(20,'ip13pro.jpg','2025-07-25 12:47:57.527409','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280673/tsh0hdhks6r0aev7pym0.jpg',_binary '\0',11,'ip13pro.jpg','2025-07-25 12:47:57.527412',5),(21,'ip13.png','2025-07-25 12:47:57.527416','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280671/tkf3amru4iijthkpznga.png',_binary '\0',12,'ip13.png','2025-07-25 12:47:57.527419',5),(22,'ip16pro.png','2025-07-25 13:43:31.727083','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280686/xdr3g3v9tu0mlguzop2l.png',_binary '',1,'ip16pro.png','2025-08-10 16:52:48.880652',6),(23,'iPhone_15_Blue_PDP_Image_Position-1__en-IN_7b4b7821-4868-4455-8806-abf70acc6a77.webp','2025-07-25 13:43:31.727129','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280688/tffdhjnkhasm6vxfkcll.webp',_binary '\0',2,'iPhone_15_Blue_PDP_Image_Position-1__en-IN_7b4b7821-4868-4455-8806-abf70acc6a77.webp','2025-08-10 16:52:48.881717',6),(24,'ip13.png','2025-07-25 13:43:31.746734','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280671/tkf3amru4iijthkpznga.png',_binary '',1,'ip13.png','2025-07-25 13:43:31.746759',7),(25,'ip13pro.jpg','2025-07-25 13:43:31.746768','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280673/tsh0hdhks6r0aev7pym0.jpg',_binary '\0',2,'ip13pro.jpg','2025-07-25 13:43:31.746772',7),(26,'ip16pro.png','2025-07-25 13:43:31.764381','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280686/xdr3g3v9tu0mlguzop2l.png',_binary '',1,'ip16pro.png','2025-07-25 13:43:31.764397',8),(27,'iPhone_15_Blue_PDP_Image_Position-1__en-IN_7b4b7821-4868-4455-8806-abf70acc6a77.webp','2025-07-25 13:43:31.764403','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280688/tffdhjnkhasm6vxfkcll.webp',_binary '\0',2,'iPhone_15_Blue_PDP_Image_Position-1__en-IN_7b4b7821-4868-4455-8806-abf70acc6a77.webp','2025-07-25 13:43:31.764407',8),(28,'product-194840.jpg','2025-07-25 13:43:31.764412','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280701/lnu5fcwmezwsmenokefw.jpg',_binary '\0',3,'product-194840.jpg','2025-07-25 13:43:31.764416',8),(29,'iPhone_15_Blue_PDP_Image_Position-1__en-IN_7b4b7821-4868-4455-8806-abf70acc6a77.webp','2025-07-25 13:43:31.782158','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280688/tffdhjnkhasm6vxfkcll.webp',_binary '',1,'iPhone_15_Blue_PDP_Image_Position-1__en-IN_7b4b7821-4868-4455-8806-abf70acc6a77.webp','2025-07-25 13:43:31.782181',9),(30,'iphone-15-1694590486.jpg','2025-07-25 13:43:31.782185','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280690/lpvxrdrkzd9mjqifb9el.jpg',_binary '\0',2,'iphone-15-1694590486.jpg','2025-07-25 13:43:31.782187',9),(31,'(600x600)_oppo_a3_white_didongmy_thumb_600x600.jpg','2025-07-25 13:43:31.798356','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280739/hcjq2gqwd1t20afuguot.jpg',_binary '',1,'(600x600)_oppo_a3_white_didongmy_thumb_600x600.jpg','2025-07-25 13:43:31.798374',10),(32,'product-194840.jpg','2025-07-25 13:43:31.798380','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280701/lnu5fcwmezwsmenokefw.jpg',_binary '\0',2,'product-194840.jpg','2025-07-25 13:43:31.798384',10),(33,'1_0f8cc785-426b-4b99-912b-ac7dcee18c6e.webp','2025-07-25 13:43:31.818622','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280741/dslvnkzbkdcxue2hgp4e.webp',_binary '',1,'1_0f8cc785-426b-4b99-912b-ac7dcee18c6e.webp','2025-07-25 13:43:31.818640',11),(34,'2_34f1083cb59d45fd8865928edd909266.png','2025-07-25 13:43:31.818647','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280746/sdvsvgni27asedlbhi2f.png',_binary '\0',2,'2_34f1083cb59d45fd8865928edd909266.png','2025-07-25 13:43:31.818650',11),(35,'iphone-15-1694590486.jpg','2025-07-25 13:43:31.835410','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280690/lpvxrdrkzd9mjqifb9el.jpg',_binary '',1,'iphone-15-1694590486.jpg','2025-07-25 13:43:31.835422',12),(36,'iphone-15-pro-max-cu-0223-zgyk-1024x1024-197422.jpg','2025-07-25 13:43:31.835427','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280692/y4ibblorrlq2hdswndya.jpg',_binary '\0',2,'iphone-15-pro-max-cu-0223-zgyk-1024x1024-197422.jpg','2025-07-25 13:43:31.835428',12),(37,'dien-thoai-oppo-chup-hinh-dep-5.jpg','2025-07-25 13:43:31.847150','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280757/ckybctj2ic533bmbeicu.jpg',_binary '',1,'dien-thoai-oppo-chup-hinh-dep-5.jpg','2025-07-25 13:43:31.847165',13),(38,'dien-thoai-oppo-find-n5_h_nh_2.webp','2025-07-25 13:43:31.847169','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280759/sii79k8xvxzqjwbbpvd3.webp',_binary '\0',2,'dien-thoai-oppo-find-n5_h_nh_2.webp','2025-07-25 13:43:31.847170',13),(39,'ba-120spl-1adr_19c9b26698b04331bae26fd00fa4c4c1_1024x1024.png','2025-08-10 16:58:05.388017','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289642/paowtfkrfzqnlt6j0kxq.png',_binary '',1,'ba-120spl-1adr_19c9b26698b04331bae26fd00fa4c4c1_1024x1024.png','2025-08-10 16:58:05.388038',14),(40,'casio-a158wa-1df-bac-1-2-700x467.jpg','2025-08-10 16:59:04.769310','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289643/cxzb5dn3aghpapz7zfks.jpg',_binary '',1,'casio-a158wa-1df-bac-1-2-700x467.jpg','2025-08-10 16:59:04.769324',15),(41,'casio-ae-1200wh-1avdf-den-nt-600x600.jpg','2025-08-10 16:59:39.390879','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289645/ccprzjefckmhrlfiq99i.jpg',_binary '',1,'casio-ae-1200wh-1avdf-den-nt-600x600.jpg','2025-08-10 16:59:39.390895',16),(42,'casio-aeq-120w-2avdf-nam-1-750x500.jpg','2025-08-10 17:00:36.803122','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289647/dboixolpn22biyucdlh6.jpg',_binary '',1,'casio-aeq-120w-2avdf-nam-1-750x500.jpg','2025-08-10 17:00:36.803140',17),(43,'dong-ho-casio-dien-tu-day-vo-nhua-f94wa9dg-1586870121.jpg','2025-08-10 17:01:12.362731','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289650/ysxvfuamscfuxkvelmas.jpg',_binary '',1,'dong-ho-casio-dien-tu-day-vo-nhua-f94wa9dg-1586870121.jpg','2025-08-10 17:01:12.362747',18),(44,'images.jpeg','2025-08-10 17:03:50.136437','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289657/avfprn7c9hnwrfbvyvaq.jpg',_binary '',1,'images.jpeg','2025-08-10 17:03:50.136448',19),(45,'MTP-E720D-8AVDF.jpg','2025-08-10 17:04:35.685241','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289659/fdyzmqnooigerzdsylbi.jpg',_binary '',1,'MTP-E720D-8AVDF.jpg','2025-08-10 17:04:35.685257',20),(46,'ws-b1000-1av_front_e569d19cafac4bf4b25a1b84624a2f48_master.png','2025-08-10 17:05:11.314256','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289662/i5pv10vk02zsryidyiva.png',_binary '',1,'ws-b1000-1av_front_e569d19cafac4bf4b25a1b84624a2f48_master.png','2025-08-10 17:05:11.314312',21),(47,'Capture-34.png','2025-08-10 17:06:56.516387','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289667/hn1dzvsg71aq6wxdyojb.png',_binary '',1,'Capture-34.png','2025-08-10 17:06:56.516405',23),(48,'dong-ho-hublot-classic-fusion-black-magic-581-cm-1171-rx.jpg','2025-08-10 17:18:16.891930','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289671/reymuasovnysbibzicf4.jpg',_binary '',1,'dong-ho-hublot-classic-fusion-black-magic-581-cm-1171-rx.jpg','2025-08-10 17:18:16.891946',24),(49,'dong-ho-hublot-classic-fusion-orlinski-titanium-40mm-550-ns-1800-rx-orl19-1.jpg','2025-08-10 17:19:05.408399','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289673/bolq6je869pkjk1i0eq7.jpg',_binary '',1,'dong-ho-hublot-classic-fusion-orlinski-titanium-40mm-550-ns-1800-rx-orl19-1.jpg','2025-08-10 17:19:05.408418',25),(50,'2023_9_13_638302015853094423_iPhone_15_Yellow_Pure_Back_iPhone_15_Yellow_Pure_Front_2up_Screen__USEN.webp','2025-08-10 18:49:58.245224','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280586/mvyemobmkmxsuo6w2kdi.webp',_binary '',1,'2023_9_13_638302015853094423_iPhone_15_Yellow_Pure_Back_iPhone_15_Yellow_Pure_Front_2up_Screen__USEN.webp','2025-08-10 18:49:58.245246',26),(51,'66855595.jpeg','2025-08-10 18:49:58.245258','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280588/tdmwwhzbjmpxtmlmfncu.jpg',_binary '\0',2,'66855595.jpeg','2025-08-10 18:49:58.245263',26),(52,'66855595.jpeg','2025-08-10 19:00:56.489899','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280588/tdmwwhzbjmpxtmlmfncu.jpg',_binary '\0',2,'66855595.jpeg','2025-08-10 19:00:56.489910',27),(53,'555248352.jpeg','2025-08-10 19:00:56.489913','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280598/uio9ytve3opqy6xzdmjw.jpg',_binary '\0',3,'555248352.jpeg','2025-08-10 19:00:56.489915',27),(54,'ip8plusbac.jpg','2025-08-10 19:04:32.871654','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280615/pag3llpihkwzxkbpvdgy.jpg',_binary '',1,'ip8plusbac.jpg','2025-08-10 19:04:32.871669',28),(55,'ip8plusden.jpg','2025-08-10 19:04:32.871677','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280617/dt2ioepecpvvd9iv5pqa.jpg',_binary '\0',2,'ip8plusden.jpg','2025-08-10 19:04:32.871696',28),(57,'555248352.jpeg','2025-08-10 22:59:37.310866','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280598/uio9ytve3opqy6xzdmjw.jpg',_binary '',1,'555248352.jpeg','2025-08-10 23:02:33.015267',29),(58,'2023_9_13_638302015853094423_iPhone_15_Yellow_Pure_Back_iPhone_15_Yellow_Pure_Front_2up_Screen__USEN.webp','2025-08-10 23:02:33.015281','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280586/mvyemobmkmxsuo6w2kdi.webp',_binary '\0',2,'2023_9_13_638302015853094423_iPhone_15_Yellow_Pure_Back_iPhone_15_Yellow_Pure_Front_2up_Screen__USEN.webp','2025-08-10 23:02:33.015286',29);
/*!40000 ALTER TABLE `product_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_reviews`
--

DROP TABLE IF EXISTS `product_reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_reviews` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `reply_to` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `is_visible` bit(1) DEFAULT NULL,
  `order_detail_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_2wu43ct4lsa85h3r5nm8nn7g6` (`order_detail_id`),
  KEY `FKr2flyju46pl1d7f7s5ynxxpby` (`reply_to`),
  KEY `FK35kxxqe2g9r4mww80w9e3tnw9` (`product_id`),
  KEY `FK58i39bhws2hss3tbcvdmrm60f` (`user_id`),
  CONSTRAINT `FK35kxxqe2g9r4mww80w9e3tnw9` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FK58i39bhws2hss3tbcvdmrm60f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKoyeivw6xbq518bx69em6m5k3h` FOREIGN KEY (`order_detail_id`) REFERENCES `order_details` (`id`),
  CONSTRAINT `FKr2flyju46pl1d7f7s5ynxxpby` FOREIGN KEY (`reply_to`) REFERENCES `product_reviews` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_reviews`
--

LOCK TABLES `product_reviews` WRITE;
/*!40000 ALTER TABLE `product_reviews` DISABLE KEYS */;
INSERT INTO `product_reviews` VALUES (11,'tot tot tot','2025-08-01 22:27:56.524616',5,'2025-08-01 22:27:56.524660',NULL,3,4,_binary '',8),(16,'tot tot tot tot','2025-08-10 10:31:39.021260',5,'2025-08-10 10:31:39.021336',NULL,1,4,_binary '',21),(17,'tot tot tot tot','2025-08-10 22:05:47.175139',5,'2025-08-10 22:05:47.175161',NULL,1,4,_binary '',39),(18,'tot tot tot','2025-08-10 22:11:12.208751',5,'2025-08-10 22:11:12.208776',NULL,1,4,_binary '',38);
/*!40000 ALTER TABLE `product_reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_variant_attribute_values`
--

DROP TABLE IF EXISTS `product_variant_attribute_values`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_variant_attribute_values` (
  `id` int NOT NULL AUTO_INCREMENT,
  `attribute_value_id` int DEFAULT NULL,
  `product_variant_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp2oicw8nmswdyc90r4hutrpb1` (`attribute_value_id`),
  KEY `FK9kcg2epv8sidgunrkkoiev00a` (`product_variant_id`),
  CONSTRAINT `FK9kcg2epv8sidgunrkkoiev00a` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`),
  CONSTRAINT `FKp2oicw8nmswdyc90r4hutrpb1` FOREIGN KEY (`attribute_value_id`) REFERENCES `variant_attribute_values` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_variant_attribute_values`
--

LOCK TABLES `product_variant_attribute_values` WRITE;
/*!40000 ALTER TABLE `product_variant_attribute_values` DISABLE KEYS */;
INSERT INTO `product_variant_attribute_values` VALUES (1,1,1),(2,3,1),(3,9,1),(4,5,2),(5,8,3),(6,1,4),(7,3,4),(8,6,4),(9,2,5),(10,3,5),(11,6,5),(12,1,6),(13,3,6),(14,6,6),(15,1,7),(16,3,7),(17,7,7),(18,1,8),(19,4,8),(20,6,8),(21,1,9),(22,4,9),(23,7,9),(24,2,10),(25,3,10),(26,6,10),(27,2,11),(28,3,11),(29,7,11),(30,2,12),(31,4,12),(32,6,12),(33,2,13),(34,4,13),(35,7,13),(36,6,14),(37,9,15),(38,9,16),(39,9,17),(40,9,18),(41,9,19),(42,9,20),(43,8,21),(44,9,22),(45,9,23),(46,7,24),(47,9,25),(48,1,26),(49,3,26),(50,1,27),(51,3,27),(52,1,28),(53,3,28),(54,1,29),(55,3,29);
/*!40000 ALTER TABLE `product_variant_attribute_values` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_variants`
--

DROP TABLE IF EXISTS `product_variants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_variants` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `sale_price` decimal(10,2) DEFAULT NULL,
  `sku` varchar(255) DEFAULT NULL,
  `sold` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `variant_name` varchar(255) DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKosqitn4s405cynmhb87lkvuau` (`product_id`),
  CONSTRAINT `FKosqitn4s405cynmhb87lkvuau` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_variants`
--

LOCK TABLES `product_variants` WRITE;
/*!40000 ALTER TABLE `product_variants` DISABLE KEYS */;
INSERT INTO `product_variants` VALUES (1,'2025-07-24 11:16:23.897083',_binary '',1000000.00,83,900000.00,'iphone-11-16gb-64gb-titan-mau-sac-titan-ram-16gb-rom-64gb',17,'2025-08-10 15:09:10.575987','Iphone 11 16GB/64GB/Titan',1),(2,'2025-07-24 12:34:29.556172',_binary '',25000000.00,0,900000.00,'may-tinh-asus-256gb-rom-256gb',2,'2025-08-10 16:52:23.510471','Máy tính Asus 256GB',2),(3,'2025-07-24 12:36:04.226647',_binary '',3000000.00,87,900000.00,'ong-ho-vang-mau-sac-vang',13,'2025-08-10 16:52:30.355381','Đồng hồ Vàng',3),(4,'2025-07-25 12:47:57.481456',_binary '',10000000.00,99,900000.00,'iphone-12-16gb-64gb-xanh-mau-sac-xanh-ram-16gb-rom-64gb',1,'2025-08-10 16:52:40.765709','Iphone 12 16GB/64GB/Xanh',4),(5,'2025-07-25 12:47:57.511102',_binary '',10000000.00,100,0.00,'iphone-12-32gb-64gb-xanh-mau-sac-xanh-ram-32gb-rom-64gb',0,'2025-07-25 12:47:57.511190','Iphone 12 32GB/64GB/Xanh',4),(6,'2025-07-25 13:43:31.707752',_binary '',1000000.00,99,900000.00,'iphone-13-16gb-64gb-xanh-mau-sac-xanh-ram-16gb-rom-64gb',1,'2025-08-10 16:52:48.879693','iphone 13 16GB/64GB/Xanh',5),(7,'2025-07-25 13:43:31.731903',_binary '',1000000.00,100,0.00,'iphone-13-16gb-64gb-o-mau-sac-o-ram-16gb-rom-64gb',0,'2025-07-25 13:43:31.731915','iphone 13 16GB/64GB/Đỏ',5),(8,'2025-07-25 13:43:31.749699',_binary '',1000000.00,100,0.00,'iphone-13-16gb-128gb-xanh-mau-sac-xanh-ram-16gb-rom-128gb',0,'2025-07-25 13:43:31.749716','iphone 13 16GB/128GB/Xanh',5),(9,'2025-07-25 13:43:31.770038',_binary '',1000000.00,100,0.00,'iphone-13-16gb-128gb-o-mau-sac-o-ram-16gb-rom-128gb',0,'2025-07-25 13:43:31.770058','iphone 13 16GB/128GB/Đỏ',5),(10,'2025-07-25 13:43:31.784497',_binary '',1000000.00,100,0.00,'iphone-13-32gb-64gb-xanh-mau-sac-xanh-ram-32gb-rom-64gb',0,'2025-07-25 13:43:31.784521','iphone 13 32GB/64GB/Xanh',5),(11,'2025-07-25 13:43:31.802286',_binary '',1000000.00,0,0.00,'iphone-13-32gb-64gb-o-mau-sac-o-ram-32gb-rom-64gb',0,'2025-07-25 13:43:31.802331','iphone 13 32GB/64GB/Đỏ',5),(12,'2025-07-25 13:43:31.822722',_binary '',1000000.00,100,0.00,'iphone-13-32gb-128gb-xanh-mau-sac-xanh-ram-32gb-rom-128gb',0,'2025-07-25 13:43:31.822755','iphone 13 32GB/128GB/Xanh',5),(13,'2025-07-25 13:43:31.836635',_binary '',1000000.00,99,0.00,'iphone-13-32gb-128gb-o-mau-sac-o-ram-32gb-rom-128gb',1,'2025-07-25 13:43:31.836644','iphone 13 32GB/128GB/Đỏ',5),(14,'2025-08-10 16:58:05.378019',_binary '',100000.00,100000,100000.00,'chronomaster-x-xanh-mau-sac-xanh',0,'2025-08-10 16:58:05.378069','ChronoMaster X Xanh',6),(15,'2025-08-10 16:59:04.764035',_binary '',1000000.00,1000000,900000.00,'titansport-pro-titan-mau-sac-titan',0,'2025-08-10 16:59:04.764053','TitanSport Pro Titan',7),(16,'2025-08-10 16:59:39.385973',_binary '',1000000.00,1000000,900000.00,'elegance-noir-titan-mau-sac-titan',0,'2025-08-10 16:59:39.385986','Elegance Noir Titan',8),(17,'2025-08-10 17:00:36.797486',_binary '',1000000.00,1000000,900000.00,'solarglide-360-titan-mau-sac-titan',0,'2025-08-10 17:00:36.797501','SolarGlide 360 Titan',9),(18,'2025-08-10 17:01:12.357718',_binary '',1000000.00,1000000,900000.00,'vintage-heritage-titan-mau-sac-titan',0,'2025-08-10 17:01:12.357733','Vintage Heritage Titan',10),(19,'2025-08-10 17:03:50.131150',_binary '',1000000.00,1000000,900000.00,'aeropilot-elite-titan-mau-sac-titan',0,'2025-08-10 17:03:50.131168','AeroPilot Elite Titan',11),(20,'2025-08-10 17:04:35.675009',_binary '',1000000.00,1000000,900000.00,'crystalwave-titan-mau-sac-titan',0,'2025-08-10 17:04:35.675031','CrystalWave Titan',12),(21,'2025-08-10 17:05:11.306915',_binary '',1000000.00,1000000,900000.00,'urbanpulse-vang-mau-sac-vang',0,'2025-08-10 17:05:11.306934','UrbanPulse Vàng',13),(22,'2025-08-10 17:05:54.259043',_binary '',1000000.00,1000000,900000.00,'omegavibe-titan-mau-sac-titan',0,'2025-08-10 17:05:54.259054','OmegaVibe Titan',14),(23,'2025-08-10 17:06:56.510146',_binary '',1000000.00,1000000,900000.00,'nexustime-titan-mau-sac-titan',0,'2025-08-10 17:06:56.510157','NexusTime Titan',15),(24,'2025-08-10 17:18:16.886511',_binary '',1000000.00,1000000,900000.00,'ong-ho-xiaomi-o-mau-sac-o',0,'2025-08-10 17:18:16.886526','Đồng hồ xiaomi  Đỏ',16),(25,'2025-08-10 17:19:05.404983',_binary '',100000.00,100000,100000.00,'ong-ho-samsung-titan-mau-sac-titan',0,'2025-08-10 17:19:05.404998','Đồng hồ samsung Titan',17),(26,'2025-08-10 18:49:58.230746',_binary '',10000000.00,100,9000000.00,'pixelstream-x-16gb-64gb-ram-16gb-rom-64gb',0,'2025-08-10 18:49:58.230767','PixelStream X 16GB/64GB',18),(27,'2025-08-10 19:00:56.484967',_binary '',100.00,100,90.00,'zenfone-aero-16gb-64gb-ram-16gb-rom-64gb',0,'2025-08-10 19:00:56.484981','Zenfone Aero 16GB/64GB',19),(28,'2025-08-10 19:04:32.866519',_binary '',100.00,100,0.00,'ivibe-12-16gb-64gb-ram-16gb-rom-64gb',0,'2025-08-10 19:04:32.866534','iVibe 12 16GB/64GB',20),(29,'2025-08-10 22:59:37.298171',_binary '',100.00,100,90.00,'ien-thoai-123-16gb-64gb-ram-16gb-rom-64gb',0,'2025-08-10 23:02:33.014557','điện thoại 123 16GB/64GB',21);
/*!40000 ALTER TABLE `product_variants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `brand` varchar(255) DEFAULT NULL,
  `content` text,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text,
  `is_active` bit(1) DEFAULT NULL,
  `is_home` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `thumbnail` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ostq1ec3toafnjok09y9l7dox` (`slug`),
  KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Apple','<p>Iphone 11</p>','2025-07-24 11:16:23.888177','Iphone 11',_binary '',_binary '','Iphone 11','iphone-11','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280620/mjffyczgeupqkos2iczf.jpg','2025-07-24 11:16:23.888189',1),(2,'Asus','<p>Máy tính Asus</p>','2025-07-24 12:34:29.527155','Máy tính Asus',_binary '',_binary '','Máy tính Asus','may-tinh-asus','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289309/dhg0p9o0w5qj73iwejuq.jpg','2025-07-24 12:34:29.527177',2),(3,'Realme','<p>Đồng hồ Vàng</p>','2025-07-24 12:36:04.224162','Đồng hồ Vàng',_binary '',_binary '','Đồng hồ Vàng','dong-ho-vang','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289654/yyrcicninzo5lt4bartn.jpg','2025-07-24 12:36:04.224174',4),(4,'Apple','<p>iphone-12</p>','2025-07-25 12:47:57.463059','iphone-12',_binary '',_binary '','Iphone 12','iphone-12','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280656/juenkgskmuiiqbz2flxj.jpg','2025-07-25 12:47:57.463073',1),(5,'Apple','<p>iphone 13</p>','2025-07-25 13:43:31.699208','iphone 13',_binary '',_binary '','iphone 13','iphone-13','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280586/mvyemobmkmxsuo6w2kdi.webp','2025-07-25 13:43:31.699280',1),(6,'Xiaomi','<p>ChronoMaster X</p>','2025-08-10 16:58:05.364860','ChronoMaster X',_binary '',_binary '','ChronoMaster X','chronomaster-x','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289647/dboixolpn22biyucdlh6.jpg','2025-08-10 16:58:05.364901',4),(7,'Samsung','<p>TitanSport Pro</p>','2025-08-10 16:59:04.761101','TitanSport Pro',_binary '',_binary '','TitanSport Pro','titansport-pro','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289643/cxzb5dn3aghpapz7zfks.jpg','2025-08-10 16:59:04.761126',4),(8,'Apple','<p>Elegance Noir</p>','2025-08-10 16:59:39.383993','Elegance Noir',_binary '',_binary '','Elegance Noir','elegance-noir','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289645/ccprzjefckmhrlfiq99i.jpg','2025-08-10 16:59:39.384008',4),(9,'Xiaomi','<p>SolarGlide 360</p>','2025-08-10 17:00:36.794788','SolarGlide 360',_binary '',_binary '','SolarGlide 360','solarglide-360','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289647/dboixolpn22biyucdlh6.jpg','2025-08-10 17:00:36.794803',4),(10,'Xiaomi','<p>Vintage Heritage</p>','2025-08-10 17:01:12.354439','Vintage Heritage',_binary '',_binary '','Vintage Heritage','vintage-heritage','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289650/ysxvfuamscfuxkvelmas.jpg','2025-08-10 17:01:12.354456',4),(11,'Xiaomi','<p>AeroPilot Elite</p>','2025-08-10 17:03:50.127893','AeroPilot Elite',_binary '',_binary '','AeroPilot Elite','aeropilot-elite','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289657/avfprn7c9hnwrfbvyvaq.jpg','2025-08-10 17:03:50.127922',4),(12,'Xiaomi','<p>CrystalWave</p>','2025-08-10 17:04:35.672547','CrystalWave',_binary '',_binary '','CrystalWave','crystalwave','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289659/fdyzmqnooigerzdsylbi.jpg','2025-08-10 17:04:35.672561',4),(13,'Samsung','<p>UrbanPulse</p>','2025-08-10 17:05:11.303800','UrbanPulse',_binary '',_binary '','UrbanPulse','urbanpulse','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289662/i5pv10vk02zsryidyiva.png','2025-08-10 17:05:11.303815',4),(14,'Samsung','<p>OmegaVibe</p>','2025-08-10 17:05:54.257463','OmegaVibe',_binary '',_binary '','OmegaVibe','omegavibe','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289664/ma0dpknkiv0ijajp4alu.jpg','2025-08-10 17:05:54.257477',4),(15,'Samsung','<p>NexusTime</p>','2025-08-10 17:06:56.507962','NexusTime',_binary '',_binary '','NexusTime','nexustime','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289667/hn1dzvsg71aq6wxdyojb.png','2025-08-10 17:06:56.507979',4),(16,'Samsung','<p>Đồng hồ xiaomi&nbsp;</p>','2025-08-10 17:18:16.882792','Đồng hồ xiaomi ',_binary '',_binary '','Đồng hồ xiaomi ','dong-ho-xiaomi','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289671/reymuasovnysbibzicf4.jpg','2025-08-10 17:18:16.882826',4),(17,'Samsung','<p>Đồng hồ samsung</p>','2025-08-10 17:19:05.402069','Đồng hồ samsung',_binary '',_binary '','Đồng hồ samsung','dong-ho-samsung','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289673/bolq6je869pkjk1i0eq7.jpg','2025-08-10 17:19:05.402085',4),(18,'Xiaomi','<p>PixelStream X</p>','2025-08-10 18:49:58.206771','PixelStream X',_binary '',_binary '','PixelStream X','pixelstream-x','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280586/mvyemobmkmxsuo6w2kdi.webp','2025-08-10 18:49:58.206798',1),(19,'Xiaomi','<p>Zenfone Aero</p>','2025-08-10 19:00:56.482383','Zenfone Aero',_binary '',_binary '','Zenfone Aero','zenfone-aero','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280586/mvyemobmkmxsuo6w2kdi.webp','2025-08-10 19:00:56.482409',1),(20,'Xiaomi','<p>iVibe 12</p>','2025-08-10 19:04:32.864401','iVibe 12',_binary '',_binary '','iVibe 12','ivibe-12','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280606/htpvbbdjswhb8tmkijlz.jpg','2025-08-10 19:04:32.864414',1),(21,'Xiaomi','<p>điện thoại 123</p>','2025-08-10 22:59:37.280012','điện thoại 123',_binary '',_binary '','điện thoại 123','dien-thoai-123','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280586/mvyemobmkmxsuo6w2kdi.webp','2025-08-10 23:02:34.765201',1);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `name` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES ('ADMIN',NULL,'Admin role',NULL,NULL),('CUSTOMER',NULL,'Customer role',NULL,NULL),('GUEST',NULL,'Guest role',NULL,NULL),('MANAGER',NULL,'Manager role',NULL,NULL),('SHIFT_STAFF',NULL,'Shift staff role',NULL,NULL);
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles_permissions`
--

DROP TABLE IF EXISTS `roles_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles_permissions` (
  `role_name` varchar(255) NOT NULL,
  `permissions_name` varchar(255) NOT NULL,
  PRIMARY KEY (`role_name`,`permissions_name`),
  KEY `FK9u1xpvjxbdnkca024o6fyr7uu` (`permissions_name`),
  CONSTRAINT `FK6nw4jrj1tuu04j9rk7xwfssd6` FOREIGN KEY (`role_name`) REFERENCES `roles` (`name`),
  CONSTRAINT `FK9u1xpvjxbdnkca024o6fyr7uu` FOREIGN KEY (`permissions_name`) REFERENCES `permissions` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles_permissions`
--

LOCK TABLES `roles_permissions` WRITE;
/*!40000 ALTER TABLE `roles_permissions` DISABLE KEYS */;
INSERT INTO `roles_permissions` VALUES ('ADMIN','ASSIGN_ROLE'),('ADMIN','ASSIGN_VOUCHER'),('CUSTOMER','BUY_PRODUCT'),('MANAGER','BUY_PRODUCT'),('SHIFT_STAFF','BUY_PRODUCT'),('ADMIN','CLAIM_VOUCHER'),('CUSTOMER','CREATE_REVIEW'),('MANAGER','CREATE_REVIEW'),('SHIFT_STAFF','CREATE_REVIEW'),('ADMIN','DELETE_USER_VOUCHER'),('MANAGER','MANAGE_ORDERS'),('SHIFT_STAFF','MANAGE_ORDERS'),('MANAGER','MANAGE_PRODUCTS'),('MANAGER','MANAGE_USERS'),('ADMIN','REPLY_PRODUCT_REVIEW'),('ADMIN','SYSTEM_BACKUP'),('ADMIN','SYSTEM_SETTINGS'),('CUSTOMER','TRACK_ORDER'),('MANAGER','TRACK_ORDER'),('SHIFT_STAFF','TRACK_ORDER'),('CUSTOMER','VIEW_ORDER'),('MANAGER','VIEW_ORDER'),('SHIFT_STAFF','VIEW_ORDER'),('CUSTOMER','VIEW_PRODUCT'),('GUEST','VIEW_PRODUCT'),('MANAGER','VIEW_PRODUCT'),('SHIFT_STAFF','VIEW_PRODUCT'),('ADMIN','VIEW_USER_VOUCHER');
/*!40000 ALTER TABLE `roles_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_logs`
--

DROP TABLE IF EXISTS `transaction_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_logs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `action_type` varchar(255) DEFAULT NULL,
  `amout` decimal(19,2) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `message` text,
  `status` varchar(255) DEFAULT NULL,
  `transaction_no` text,
  `transaction_ref` text,
  `type` int DEFAULT NULL,
  `order_id` int DEFAULT NULL,
  `return_id` int DEFAULT NULL,
  `payment_method_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4jmdhugdx9ankk4hbri0dy40k` (`order_id`),
  KEY `FKsvknwk34u0c9k6ctwg1884nrg` (`return_id`),
  KEY `FK38xqr5k64fcqwhi8fp8aji1dg` (`payment_method_id`),
  CONSTRAINT `FK38xqr5k64fcqwhi8fp8aji1dg` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_methods` (`id`),
  CONSTRAINT `FK4jmdhugdx9ankk4hbri0dy40k` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKsvknwk34u0c9k6ctwg1884nrg` FOREIGN KEY (`return_id`) REFERENCES `order_returns` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_logs`
--

LOCK TABLES `transaction_logs` WRITE;
/*!40000 ALTER TABLE `transaction_logs` DISABLE KEYS */;
INSERT INTO `transaction_logs` VALUES (1,'CREATE ORDER ',3000000.00,'2025-07-24 23:12:48.393533','CREATE ORDER 1','PENDING',NULL,NULL,1,1,NULL,1),(2,'CREATE ORDER ',1000000.00,'2025-07-25 13:27:04.173829','CREATE ORDER 2','PENDING',NULL,NULL,1,2,NULL,1),(3,'CREATE ORDER ',65000000.00,'2025-07-25 14:39:47.795908','CREATE ORDER 3','PENDING',NULL,NULL,1,3,NULL,1),(4,'CREATE ORDER ',1000000.00,'2025-07-25 14:56:04.027871','CREATE ORDER 4','PENDING',NULL,'1753430144893',1,4,NULL,2),(5,'CREATE ORDER ',1000000.00,'2025-07-25 14:57:16.468678','CREATE ORDER 5','PENDING',NULL,'1753430227863',1,5,NULL,2),(6,'CREATE ORDER ',1000000.00,'2025-07-25 14:58:39.370458','CREATE ORDER 6','PENDING',NULL,NULL,1,6,NULL,1),(7,'CREATE ORDER ',3000000.00,'2025-07-25 15:02:04.981164','CREATE ORDER 7','PENDING',NULL,'1753430502948',1,7,NULL,2),(8,'PAYMENT SUCCESS ',3000000.00,'2025-07-25 15:05:57.666523','PAYMENT SUCCESS 7','PENDING','15099195','1753430502948',1,7,NULL,2),(9,'PAYMENT SUCCESS ',3000000.00,'2025-07-25 15:06:22.651987','PAYMENT SUCCESS 7','PENDING','15099195','1753430502948',1,7,NULL,2),(10,'UPDATE STATUS ',3000000.00,'2025-07-25 15:16:54.409770','UPDATE STATUS 7','CONFIRMED',NULL,NULL,1,7,NULL,2),(11,'UPDATE STATUS ',3000000.00,'2025-07-25 15:17:10.762377','UPDATE STATUS 7','SHIPPED',NULL,NULL,1,7,NULL,2),(12,'UPDATE STATUS ',3000000.00,'2025-07-25 15:17:12.639983','UPDATE STATUS 7','DELIVERED',NULL,NULL,1,7,NULL,2),(13,'UPDATE STATUS ',3000000.00,'2025-07-25 15:17:13.486665','UPDATE STATUS 7','RECEIED',NULL,NULL,1,7,NULL,2),(14,'CREATE ORDER ',1000000.00,'2025-07-25 15:37:56.470305','CREATE ORDER 8','PENDING',NULL,'1753432657753',1,8,NULL,2),(15,'CREATE ORDER ',1000000.00,'2025-07-25 15:41:25.038134','CREATE ORDER 9','PENDING',NULL,'1753432869284',1,9,NULL,2),(16,'CREATE ORDER ',1000000.00,'2025-07-25 15:45:45.323753','CREATE ORDER 10','PENDING',NULL,'1753433124537',1,10,NULL,2),(17,'CREATE ORDER ',1000000.00,'2025-07-25 15:48:21.133318','CREATE ORDER 11','PENDING',NULL,NULL,1,11,NULL,1),(18,'CREATE ORDER ',1000000.00,'2025-07-25 15:48:56.606936','CREATE ORDER 12','PENDING',NULL,'1753433320451',1,12,NULL,2),(19,'CREATE ORDER ',10000000.00,'2025-07-25 15:56:00.938975','CREATE ORDER 13','PENDING',NULL,'1753433752045',1,13,NULL,2),(20,'CREATE ORDER ',2000000.00,'2025-07-25 21:04:18.931154','CREATE ORDER 14','CONFIRMED',NULL,NULL,1,14,NULL,1),(21,'CREATE ORDER ',1000000.00,'2025-07-25 21:09:15.718620','CREATE ORDER 15','PENDING',NULL,'1753452551942',1,15,NULL,2),(22,'CREATE ORDER ',1000000.00,'2025-07-25 21:25:24.391447','CREATE ORDER 16','PENDING',NULL,'1753453513288',1,16,NULL,2),(23,'CREATE ORDER ',6000000.00,'2025-07-25 21:37:01.748612','CREATE ORDER 17','PENDING',NULL,'1753454204420',1,17,NULL,2),(24,'CREATE ORDER ',3000000.00,'2025-07-25 21:42:59.816675','CREATE ORDER 18','PENDING',NULL,'1753454554600',1,18,NULL,2),(25,'CREATE ORDER ',1000000.00,'2025-07-25 21:51:40.705267','CREATE ORDER 19','PENDING',NULL,'1753455079228',1,19,NULL,2),(26,'UPDATE STATUS ',1000000.00,'2025-07-25 21:52:16.099613','UPDATE STATUS 19','PENDING',NULL,NULL,1,19,NULL,2),(27,'UPDATE STATUS ',1000000.00,'2025-07-31 20:13:26.580843','UPDATE STATUS 15','RECEIED',NULL,NULL,1,15,NULL,2),(28,'CANCELLED ',1000000.00,'2025-07-31 20:13:32.108210','CANCELLED 16','CANCELLED',NULL,NULL,1,16,NULL,2),(29,'UPDATE STATUS ',1000000.00,'2025-08-01 21:41:54.791125','UPDATE STATUS 19','CONFIRMED',NULL,NULL,1,19,NULL,2),(30,'UPDATE STATUS ',1000000.00,'2025-08-01 21:42:02.644587','UPDATE STATUS 19','SHIPPED',NULL,NULL,1,19,NULL,2),(31,'UPDATE STATUS ',1000000.00,'2025-08-01 21:42:04.717616','UPDATE STATUS 19','DELIVERED',NULL,NULL,1,19,NULL,2),(32,'UPDATE STATUS ',1000000.00,'2025-08-01 21:42:08.711955','UPDATE STATUS 19','RECEIED',NULL,NULL,1,19,NULL,2),(33,'CANCELLED ',3000000.00,'2025-08-01 22:30:50.464396','CANCELLED 18','CANCELLED',NULL,NULL,1,18,NULL,2),(34,'CREATE ORDER ',990000.00,'2025-08-05 02:01:27.556030','CREATE ORDER 20','PENDING',NULL,NULL,1,20,NULL,1),(35,'CREATE ORDER ',2970000.00,'2025-08-05 02:03:45.056015','CREATE ORDER 21','PENDING',NULL,NULL,1,21,NULL,1),(36,'CREATE ORDER ',3000000.00,'2025-08-05 02:05:32.784637','CREATE ORDER 22','PENDING',NULL,NULL,1,22,NULL,1),(37,'CREATE ORDER ',3000000.00,'2025-08-05 02:06:00.840707','CREATE ORDER 23','PENDING',NULL,NULL,1,23,NULL,1),(38,'CREATE ORDER ',1000000.00,'2025-08-05 02:10:44.684103','CREATE ORDER 24','PENDING',NULL,'1754334639425',1,24,NULL,2),(39,'CREATE ORDER ',3000000.00,'2025-08-05 02:13:31.161001','CREATE ORDER 25','PENDING',NULL,NULL,1,25,NULL,1),(40,'CREATE ORDER ',3000000.00,'2025-08-05 02:13:43.570829','CREATE ORDER 26','PENDING',NULL,'1754334819127',1,26,NULL,2),(41,'CREATE ORDER ',3000000.00,'2025-08-07 17:14:42.986607','CREATE ORDER 27','PENDING',NULL,'1754561677143',1,27,NULL,2),(42,'CREATE ORDER ',1000000.00,'2025-08-07 17:20:01.456261','CREATE ORDER 28','PENDING',NULL,'1754561996369',1,28,NULL,2),(43,'CREATE ORDER ',1000000.00,'2025-08-07 17:24:13.874590','CREATE ORDER 29','PENDING',NULL,'1754562249854',1,29,NULL,2),(44,'UPDATE STATUS ',1000000.00,'2025-08-07 17:25:38.683342','UPDATE STATUS 29','PENDING',NULL,NULL,1,29,NULL,2),(45,'CANCELLED ',1000000.00,'2025-08-08 13:50:20.262934','CANCELLED 29','CANCELLED',NULL,NULL,1,29,NULL,2),(46,'CANCELLED ',1000000.00,'2025-08-08 13:53:13.655706','CANCELLED 28','CANCELLED',NULL,NULL,1,28,NULL,2),(47,'CANCELLED ',3000000.00,'2025-08-08 13:55:08.047676','CANCELLED 27','CANCELLED',NULL,NULL,1,27,NULL,2),(48,'CANCELLED ',3000000.00,'2025-08-08 13:56:16.594894','CANCELLED 26','CANCELLED',NULL,NULL,1,26,NULL,2),(49,'CANCELLED ',3000000.00,'2025-08-08 13:56:36.228568','CANCELLED 25','CANCELLED',NULL,NULL,1,25,NULL,1),(50,'CREATE ORDER ',10890000.00,'2025-08-08 16:16:00.076364','CREATE ORDER 30','PENDING',NULL,'1754644554594',1,30,NULL,2),(51,'UPDATE STATUS ',10890000.00,'2025-08-08 16:16:35.818469','UPDATE STATUS 30','PENDING',NULL,NULL,1,30,NULL,2),(52,'CANCELLED ',10890000.00,'2025-08-09 03:51:51.282382','CANCELLED 30','CANCELLED',NULL,NULL,1,30,NULL,2),(53,'CANCELLED ',1000000.00,'2025-08-09 04:03:18.102952','CANCELLED 24','CANCELLED',NULL,NULL,1,24,NULL,2),(54,'CREATE ORDER ',1000000.00,'2025-08-09 04:09:35.880045','CREATE ORDER 31','CONFIRMED',NULL,NULL,1,31,NULL,1),(55,'CANCELLED ',1000000.00,'2025-08-09 04:12:01.128277','CANCELLED 31','CANCELLED',NULL,NULL,1,31,NULL,1),(56,'CANCELLED ',3000000.00,'2025-08-09 04:13:53.512311','CANCELLED 22','CANCELLED',NULL,NULL,1,22,NULL,1),(57,'CREATE ORDER ',990000.00,'2025-08-09 12:34:17.638218','CREATE ORDER 32','PENDING',NULL,NULL,1,32,NULL,1),(58,'CREATE ORDER ',3000000.00,'2025-08-09 12:36:38.127970','CREATE ORDER 33','PENDING',NULL,NULL,1,33,NULL,1),(59,'CREATE ORDER ',3000000.00,'2025-08-09 12:36:54.103246','CREATE ORDER 34','PENDING',NULL,NULL,1,34,NULL,1),(60,'CREATE ORDER ',1000000.00,'2025-08-09 15:11:35.534395','CREATE ORDER 35','PENDING',NULL,NULL,1,35,NULL,1),(61,'CREATE ORDER ',990000.00,'2025-08-09 15:11:56.420424','CREATE ORDER 36','PENDING',NULL,NULL,1,36,NULL,1),(62,'UPDATE STATUS ',3000000.00,'2025-08-09 15:22:06.443672','UPDATE STATUS 23','RECEIED',NULL,NULL,1,23,NULL,1),(63,'UPDATE STATUS ',990000.00,'2025-08-09 21:42:42.566956','UPDATE STATUS 36','RECEIED',NULL,NULL,1,36,NULL,1),(64,'CREATE ORDER ',990000.00,'2025-08-09 21:45:02.426522','CREATE ORDER 37','PENDING',NULL,NULL,1,37,NULL,1),(65,'CREATE ORDER ',1000000.00,'2025-08-10 10:09:14.384151','CREATE ORDER 38','PENDING',NULL,NULL,1,38,NULL,1),(66,'CREATE ORDER ',990000.00,'2025-08-10 10:14:41.147063','CREATE ORDER 39','PENDING',NULL,NULL,1,39,NULL,1),(67,'CANCELLED ',990000.00,'2025-08-10 21:39:40.421201','CANCELLED 37','CANCELLED',NULL,NULL,1,37,NULL,1),(68,'UPDATE STATUS ',1000000.00,'2025-08-10 22:10:42.397639','UPDATE STATUS 35','RECEIED',NULL,NULL,1,35,NULL,1);
/*!40000 ALTER TABLE `transaction_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upload_images`
--

DROP TABLE IF EXISTS `upload_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `upload_images` (
  `id` int NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) DEFAULT NULL,
  `public_id` varchar(255) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=215 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upload_images`
--

LOCK TABLES `upload_images` WRITE;
/*!40000 ALTER TABLE `upload_images` DISABLE KEYS */;
INSERT INTO `upload_images` VALUES (1,'2023_9_13_638302015853094423_iPhone_15_Yellow_Pure_Back_iPhone_15_Yellow_Pure_Front_2up_Screen__USEN.webp','mvyemobmkmxsuo6w2kdi','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280586/mvyemobmkmxsuo6w2kdi.webp'),(2,'66855595.jpeg','tdmwwhzbjmpxtmlmfncu','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280588/tdmwwhzbjmpxtmlmfncu.jpg'),(3,'555248352.jpeg','uio9ytve3opqy6xzdmjw','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280598/uio9ytve3opqy6xzdmjw.jpg'),(4,'872124681.jpeg','ab0ufhbmhsq0qrmwpdcz','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280601/ab0ufhbmhsq0qrmwpdcz.jpg'),(5,'ip6bac.webp','ueumyvznuooz6asmq4t4','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280603/ueumyvznuooz6asmq4t4.webp'),(6,'ip6vang.jpg','cfk4znr9jfcmggevuzn4','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280605/cfk4znr9jfcmggevuzn4.jpg'),(7,'ip7hong.jpg','htpvbbdjswhb8tmkijlz','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280606/htpvbbdjswhb8tmkijlz.jpg'),(8,'ip7plus.jpg','oxipvtnmccn03vxbn4ld','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280607/oxipvtnmccn03vxbn4ld.webp'),(9,'ip7plusdo.jpg','rmiwyfztonl5nmvrawgh','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280609/rmiwyfztonl5nmvrawgh.jpg'),(10,'ip7vang.jpg','opvq7ya9nhzqat1gnpax','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280611/opvq7ya9nhzqat1gnpax.jpg'),(11,'ip8plus.png','ejem4gkpdwppxfsosiej','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280614/ejem4gkpdwppxfsosiej.png'),(12,'ip8plusbac.jpg','pag3llpihkwzxkbpvdgy','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280615/pag3llpihkwzxkbpvdgy.jpg'),(13,'ip8plusden.jpg','dt2ioepecpvvd9iv5pqa','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280617/dt2ioepecpvvd9iv5pqa.jpg'),(14,'ip11bac.jpg','zi8rki3jb1xigsu5z1zz','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280618/zi8rki3jb1xigsu5z1zz.jpg'),(15,'ip11tim.jpg','mjffyczgeupqkos2iczf','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280620/mjffyczgeupqkos2iczf.jpg'),(16,'ip11trang.png','fov8i6ipubi3okjxde3c','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280621/fov8i6ipubi3okjxde3c.webp'),(17,'ip11xanh.jpeg','epuzw3hw0woeffnphf1x','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280655/epuzw3hw0woeffnphf1x.jpg'),(18,'ip12do.jpg','juenkgskmuiiqbz2flxj','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280656/juenkgskmuiiqbz2flxj.jpg'),(19,'ip12pro.jpg','shoflqdngfctlxtbrbr8','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280659/shoflqdngfctlxtbrbr8.jpg'),(20,'ip12proden.jpg','wjc3iqmnq3svjffv9kvr','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280661/wjc3iqmnq3svjffv9kvr.jpg'),(21,'ip12trang.jpg','w5ezjzieyfn8yrcaylfg','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280662/w5ezjzieyfn8yrcaylfg.webp'),(22,'ip12trang.png','xdi5a5v0rys2uhcxlnq9','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280665/xdi5a5v0rys2uhcxlnq9.png'),(23,'ip12xanh.png','wucte93ngxu0ouxecfdd','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280667/wucte93ngxu0ouxecfdd.png'),(24,'ip13.jpg','tuxros9sq8lyxlloeeh5','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280669/tuxros9sq8lyxlloeeh5.jpg'),(25,'ip13.png','tkf3amru4iijthkpznga','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280671/tkf3amru4iijthkpznga.png'),(26,'ip13pro.jpg','tsh0hdhks6r0aev7pym0','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280673/tsh0hdhks6r0aev7pym0.jpg'),(27,'ip13promax.png','asm4qvrsbalpjebm4eay','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280676/asm4qvrsbalpjebm4eay.png'),(28,'ip13xanh.jpg','hwjpdd2fbil4gasqwhlt','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280678/hwjpdd2fbil4gasqwhlt.jpg'),(29,'ip14protrang.jpg','qm6dhwacop1e6q73lexv','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280680/qm6dhwacop1e6q73lexv.jpg'),(30,'ip15pro.jpg','ho1djda8xvaxawlly0am','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280682/ho1djda8xvaxawlly0am.jpg'),(31,'ip15pro.webp','w96jchpzznhqgw3rsuk0','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280684/w96jchpzznhqgw3rsuk0.webp'),(32,'ip16pro.png','xdr3g3v9tu0mlguzop2l','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280686/xdr3g3v9tu0mlguzop2l.png'),(33,'iPhone_15_Blue_PDP_Image_Position-1__en-IN_7b4b7821-4868-4455-8806-abf70acc6a77.webp','tffdhjnkhasm6vxfkcll','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280688/tffdhjnkhasm6vxfkcll.webp'),(34,'iphone-15-1694590486.jpg','lpvxrdrkzd9mjqifb9el','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280690/lpvxrdrkzd9mjqifb9el.jpg'),(35,'iphone-15-pro-max-cu-0223-zgyk-1024x1024-197422.jpg','y4ibblorrlq2hdswndya','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280692/y4ibblorrlq2hdswndya.jpg'),(36,'iphone-16-teal-128gb.jpg','g8p7c8f51qcazuz6wyx3','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280694/g8p7c8f51qcazuz6wyx3.jpg'),(37,'ipxbac.png','uj4tbj7psdmsn0nbihrf','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280697/uj4tbj7psdmsn0nbihrf.png'),(38,'ipxden.png','nzsar3puurdeuakmbuvi','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280700/nzsar3puurdeuakmbuvi.png'),(39,'product-194840.jpg','lnu5fcwmezwsmenokefw','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280701/lnu5fcwmezwsmenokefw.jpg'),(40,'(600x600)_oppo_a3_white_didongmy_thumb_600x600.jpg','hcjq2gqwd1t20afuguot','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280739/hcjq2gqwd1t20afuguot.jpg'),(41,'1_0f8cc785-426b-4b99-912b-ac7dcee18c6e.webp','dslvnkzbkdcxue2hgp4e','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280741/dslvnkzbkdcxue2hgp4e.webp'),(42,'2_34f1083cb59d45fd8865928edd909266.png','sdvsvgni27asedlbhi2f','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280746/sdvsvgni27asedlbhi2f.png'),(43,'43-de62f430-02b5-4296-ad68-8e01f635f8a5.webp','dwalzanfsrp0wqu9jcit','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280753/dwalzanfsrp0wqu9jcit.webp'),(44,'51A+YaFYKnL._UF894,1000_QL80_.jpg','hrzyieiobe77epzlmjyz','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280754/hrzyieiobe77epzlmjyz.jpg'),(45,'dien-thoai-di-dong-oppo-reno14f-5g-8256-cph2743---mau-xanh-duong-dm_57b520ee.webp','gq6rpdxpsax8rtmpho3j','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280755/gq6rpdxpsax8rtmpho3j.webp'),(46,'dien-thoai-oppo-chup-hinh-dep-5.jpg','ckybctj2ic533bmbeicu','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280757/ckybctj2ic533bmbeicu.jpg'),(47,'dien-thoai-oppo-find-n5_h_nh_2.webp','sii79k8xvxzqjwbbpvd3','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280759/sii79k8xvxzqjwbbpvd3.webp'),(48,'gs-009284-desktop-102848.jpg','ikz38h9jlzd4vka5kusc','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280761/ikz38h9jlzd4vka5kusc.jpg'),(49,'oppo_a3_tim_5_a81a5f4bf7.jpg','fx3iyhqvuom7uslh8qvg','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280764/fx3iyhqvuom7uslh8qvg.jpg'),(50,'oppo_find_x8_space_black_1_6a9c3746b3.png','hyjedmfceutomr1gu2nl','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280767/hyjedmfceutomr1gu2nl.png'),(51,'oppo_reno_12_blue-.jpg','oxjv9e2femuwde2xb7k0','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280770/oxjv9e2femuwde2xb7k0.jpg'),(52,'oppo_reno_13f_den_7da96b2c87.png','wwazrufokrgp3np2lsm4','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280773/wwazrufokrgp3np2lsm4.png'),(53,'oppo_reno6.webp','hlxphz0hwvfujbedhcdd','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280775/hlxphz0hwvfujbedhcdd.webp'),(54,'oppo-a3x-red-thumb-600x600.jpg','xfejofzvub5cvhh2of6e','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280777/xfejofzvub5cvhh2of6e.jpg'),(55,'Oppo-A5-2019-Quoc-Te-Ram-4GB-–-Bo-Nho-64-GB.jpg','ngkhyshgypkxze3qodwg','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280779/ngkhyshgypkxze3qodwg.jpg'),(56,'OPPO-A5i-Pro.Pur1.jpg','tpbpoesjyizkzdsugqz6','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280781/tpbpoesjyizkzdsugqz6.jpg'),(57,'oppo-a5-pro-pink-thumbai-600x600.jpg','dzsf0fqex9ugka9ddyge','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280783/dzsf0fqex9ugka9ddyge.jpg'),(58,'oppo-a79-5g-tim-thumb-1-2-600x600.jpg','wgm7u52f1seoimojea9g','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280784/wgm7u52f1seoimojea9g.jpg'),(59,'oppo-cph2591-128gb-a18-xanh_1e00c8e9.webp','cpjzvdjdtrznhwrnaahj','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280786/cpjzvdjdtrznhwrnaahj.webp'),(60,'oppo-find-x7-ultra-13.webp','gffpkxycir7zmyxojgxb','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280788/gffpkxycir7zmyxojgxb.webp'),(61,'oppo-reno10-pro-plus-tim.webp','cstmmlpc2ijsvlhgzmj7','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280789/cstmmlpc2ijsvlhgzmj7.webp'),(62,'Oppo-Reno12-5G-xam.png','lnafsbtrulvf21io5vyy','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280792/lnafsbtrulvf21io5vyy.png'),(63,'oppo-reno-13f-5g-xam.png','lcrdwvzrzm1c6jdyesqr','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280797/lcrdwvzrzm1c6jdyesqr.png'),(64,'oppo-reoo13-pro-5g-purple-thumbnew-600x600.jpg','ueslwxx4wbhegaosphuu','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280799/ueslwxx4wbhegaosphuu.jpg'),(65,'reno_9_black_d899f019466347de812ebb2c7904107b_master.png','ee3vzna2cdxyktkmgr0v','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280802/ee3vzna2cdxyktkmgr0v.png'),(66,'13-354e53c3-7c9a-4207-99c5-43a3a53e5f30.webp','usqkrcabeuptc8fpti5x','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280832/usqkrcabeuptc8fpti5x.webp'),(67,'61XyNdmvr6L.jpg','eg6efqnkx1bdzlqkgib8','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280834/eg6efqnkx1bdzlqkgib8.jpg'),(68,'556955088.jpeg','esq4lf0sen35lv0ke63b','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280836/esq4lf0sen35lv0ke63b.jpg'),(69,'D_NQ_NP_739465-MLA82773510322_032025-O.webp','ud7ehwmlivpergfpuhtl','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280837/ud7ehwmlivpergfpuhtl.webp'),(70,'dien-thoai-realme-c61_2_.webp','r3pibgwpuiwja1asxg3a','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280838/r3pibgwpuiwja1asxg3a.webp'),(71,'dien-thoai-realme-c65s_3_.webp','q1g09m1h8jbkfmczj8ts','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280840/q1g09m1h8jbkfmczj8ts.webp'),(72,'images.jpeg','blgnh1gqn3dwdx3cfih5','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280842/blgnh1gqn3dwdx3cfih5.jpg'),(73,'-original-imah56hkgehywn5b.webp','rf03gc4snoee6arkjc8c','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280844/rf03gc4snoee6arkjc8c.webp'),(74,'realme_c71_den_3_be93234e7b.png','zp5p4p0laklvbenhq8js','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280847/zp5p4p0laklvbenhq8js.png'),(75,'realme-7.png','jq6ldzi0dqunt2juroxn','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280850/jq6ldzi0dqunt2juroxn.png'),(76,'realme-11-pro-plus-5g-new-100-nguyen-seal-fullbox.jpeg','sp6iahkqgktujcs0egfh','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280851/sp6iahkqgktujcs0egfh.jpg'),(77,'realme-c61-4gb1_main_252_1020.png.webp','rfwzanxjdn49e1xoc8nm','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280853/rfwzanxjdn49e1xoc8nm.webp'),(78,'realme-c65-purple-thumb-600x600.jpg','z9q4fmypmydazppwsgob','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280854/z9q4fmypmydazppwsgob.jpg'),(79,'realme-neo7---trắng_1734263372.jpg.jpg','fb3j4bqkwrnaxtbv9vai','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280857/fb3j4bqkwrnaxtbv9vai.jpg'),(80,'(600x600)_samsung_galaxy_a16_5g_trang_thumb_1.jpg','zwlfa0lnwws5xptdjalp','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280876/zwlfa0lnwws5xptdjalp.jpg'),(81,'LD0006204287.jpg','m04amns275ec8ffkkcfu','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280878/m04amns275ec8ffkkcfu.jpg'),(82,'REGEN-GalaxyS24Ultra-TitaniumBlack.webp','uwdbwdlnhu6v1bq4xcke','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280880/uwdbwdlnhu6v1bq4xcke.webp'),(83,'s25u_2.jpg','l7qhisego0hedhoafk8j','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280882/l7qhisego0hedhoafk8j.jpg'),(84,'samsung_galaxy_a16_5g_gold_.jpg','lmigl5swhpysj4uuxzc9','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280883/lmigl5swhpysj4uuxzc9.jpg'),(85,'Samsung-galaxy-A16.webp','ks82ilthejnbt9fl625t','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280885/ks82ilthejnbt9fl625t.webp'),(86,'samsung-galaxy-a16-gray-thumb-600x600.jpg','lnn5ovw5iftjf2l8jwwu','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280887/lnn5ovw5iftjf2l8jwwu.jpg'),(87,'Samsung-Galaxy-A53-5G-8GB-256GB-Awesome-White-1.jpg','nlywad9exal1a7pmlzel','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280890/nlywad9exal1a7pmlzel.jpg'),(88,'Samsung-Galaxy-A55-5G-c.jpg','pxa9pxdjdl6w6ucedjel','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280893/pxa9pxdjdl6w6ucedjel.jpg'),(89,'samsung-galaxy-a56-5g-xanh.jpg.webp','jj4mxyn5ahr9cffwutsy','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280895/jj4mxyn5ahr9cffwutsy.webp'),(90,'Samsung-Galaxy-Note-10-256GB-RAM-12GB-Ban-2-Sim.jpg','itczvyljxgcmrzyjvn7t','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280897/itczvyljxgcmrzyjvn7t.jpg'),(91,'Samsung-Galaxy-S24-Ultra-256GB-Titanium-Violet-3.webp','ljcumqqbzh8uidftyi0q','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280898/ljcumqqbzh8uidftyi0q.webp'),(92,'Samsung-Galaxy-S25-Ultra-Titan-Blue.jpg','zi3f6tpwyzll4hmvwpax','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280901/zi3f6tpwyzll4hmvwpax.jpg'),(93,'sms908galaxys22ultrafrontburgu-3f4d54df-c1cc-40a5-afe9-7b8c7a5ac3ac_d51cf809b1df42e78cf44c6bdb806fb0_large.jpg','zinb19c5cmdhh1go9qjm','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280902/zinb19c5cmdhh1go9qjm.jpg'),(94,'ss-s24-ultra-xam-222_1.webp','ncng3af9bqrikujgvsxe','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280903/ncng3af9bqrikujgvsxe.webp'),(95,'vn-galaxy-s25-s937-sm-s937bzscxxv-thumb-546085978.webp','cpxhfivksnbde2iui9kg','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280905/cpxhfivksnbde2iui9kg.webp'),(96,'(600x600)_vivo_v40_5g_didongmy_thumb_600x600_1.jpg','db55yumtttmuztrib0jr','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280925/db55yumtttmuztrib0jr.jpg'),(97,'(600x600)_vivo_v50_lite_tim_thumb_600x600_1.jpg','ufnwnpt7byptpg8uzvsh','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280927/ufnwnpt7byptpg8uzvsh.jpg'),(98,'8db3ee8cbec1258c3cd682b089ce5c8c_36bcb62936ab484da3ed6c8f025d0a22_master.jpg','l5mkx76o4rzko1jujx6b','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280929/l5mkx76o4rzko1jujx6b.jpg'),(99,'4693505510523148078.jpg','leeg1p8s0kzmlel8mrp5','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280932/leeg1p8s0kzmlel8mrp5.jpg'),(100,'c6df355dae028c9f36232273e4280565.png','itji3klblntobu2hjxrc','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280936/itji3klblntobu2hjxrc.png'),(101,'dien-thoai-vivo_main_259_1020.png.webp','dnvscllajphzv7gns5vr','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280939/dnvscllajphzv7gns5vr.webp'),(102,'dien-thoai-vivo-y19s_1_1.webp','r5jji7gmedozflne5qru','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280940/r5jji7gmedozflne5qru.webp'),(103,'image-removebg-preview_-_2024-11-01T120246.193.avif','y3lw4ps1thstfeljlsd3','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280941/y3lw4ps1thstfeljlsd3.avif'),(104,'images.jpeg','p22bwxbahz04dwz0ezla','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280943/p22bwxbahz04dwz0ezla.jpg'),(105,'vivo-S17e-duchuymobile.jpg','cr9uira0uboga5cowhqf','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280945/cr9uira0uboga5cowhqf.jpg'),(106,'vivo-v50-lite-gold-5g-thumbai-600x600.jpg','ul9kjoxsjhtxt3cxva65','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280947/ul9kjoxsjhtxt3cxva65.jpg'),(107,'vivo-x200-pro-11.webp','xfyhju3ilyy0jmcnekby','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280949/xfyhju3ilyy0jmcnekby.webp'),(108,'vivo-x200-ultra-mau-den.jpg','dhrp5f01krapnxp7gu8u','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280952/dhrp5f01krapnxp7gu8u.jpg'),(109,'Vivo-Y21-xanh.png','bdwdhscerhft3thm8lts','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280955/bdwdhscerhft3thm8lts.png'),(110,'vivo-y28-xanh-thumbn-600x600.jpg','bbr9yrdciwclgo02bxlk','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280956/bbr9yrdciwclgo02bxlk.jpg'),(111,'Vivo-Y36-grn1.jpg','ejd9fjfwesqiaqfegqar','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280958/ejd9fjfwesqiaqfegqar.jpg'),(112,'vivo-y50-cu.jpg','tqz1veqsjrqjg5csjbo6','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280960/tqz1veqsjrqjg5csjbo6.jpg'),(113,'vivo-y78-chinh-hang-duchuymobile.jpg','zv2beqo54vg3gqukhhta','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280961/zv2beqo54vg3gqukhhta.jpg'),(114,'y02a.jpeg','elyrhm8tknznaomceuas','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280963/elyrhm8tknznaomceuas.jpg'),(115,'(600x600)_crop_x15.webp','yvflyffvixzl7mpwnbcb','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280964/yvflyffvixzl7mpwnbcb.webp'),(116,'0b3d3dddd20d05a75e1454cec993cda1.jpg','zbo4xq47btjy4d5d9tpa','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280966/zbo4xq47btjy4d5d9tpa.jpg'),(117,'0019812_xiaomi-14t.png','dbpj5p7aq2ijcayw1lyb','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280969/dbpj5p7aq2ijcayw1lyb.png'),(118,'5476880.png','ehxgu6en8h5y2ernxty4','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280971/ehxgu6en8h5y2ernxty4.png'),(119,'10057444-dien-thoai-xiaomi-red-note-13pro_-5g-8gb-256gb-den-1.webp','uju48ufunnximystr5nb','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280973/uju48ufunnximystr5nb.webp'),(120,'images.jpeg','avckncpxih962tc3a9mv','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280975/avckncpxih962tc3a9mv.jpg'),(121,'photo_2025-04-16_11-45-37.webp','dbqkecgd5fi0yiihv8pz','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280977/dbqkecgd5fi0yiihv8pz.webp'),(122,'redmi-k70e-trang_1701408301.jpg.jpg','lw0icubuzgzmojb9d7br','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280979/lw0icubuzgzmojb9d7br.jpg'),(123,'tim-3.jpg','zakal9lkhtvsdglrpndy','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280981/zakal9lkhtvsdglrpndy.jpg'),(124,'xiaomi_14t_2_.webp','ywzwcmkhkuuiaitvw7tv','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280982/ywzwcmkhkuuiaitvw7tv.webp'),(125,'xiaomi_redmi_note_14_5g_xanh_3_a16f31cae7.jpg','uqzu2bsyjpmxzbdnt4sa','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280985/uqzu2bsyjpmxzbdnt4sa.jpg'),(126,'xiaomi-14t-titan-xanh.jpg.webp','klh8tuoqswznfgdcnpu5','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280987/klh8tuoqswznfgdcnpu5.webp'),(127,'xiaomi-14-ultra-den.jpg.webp','c52utl30cagjxkwonqmf','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280988/c52utl30cagjxkwonqmf.webp'),(128,'xiaomi-14-viettablet-8-256_optimized.webp','xwpmreu4ou9vdkkwy8gl','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280992/xwpmreu4ou9vdkkwy8gl.webp'),(129,'xiaomi-15-white-thumbnew-600x600.jpg','fglf894hhe1opnvexks0','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280993/fglf894hhe1opnvexks0.jpg'),(130,'xiaomi-redmi-12-5g-trang_jpg.avif','yliywgy1usuylbgnxa2n','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280995/yliywgy1usuylbgnxa2n.avif'),(131,'xiaomi-redmi-13-1.webp','kdew6pv61jjfeiwokzmf','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280996/kdew6pv61jjfeiwokzmf.webp'),(132,'xiaomi-redmi-k40-gaming_e0ae5930376843438e80e92e2b2d6ce1_master.jpg','ashwlmpfs5pcuyc1qaza','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753280998/ashwlmpfs5pcuyc1qaza.jpg'),(133,'xiaomi-redmi-note-13-pro-5g-4.jpg','p65c6d2krrtvwtoa6mim','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753281000/p65c6d2krrtvwtoa6mim.jpg'),(134,'xiaomi-redmi-note-13-pro-8gb-256gb-chinh-hang (6)_1706037591_1.jpg','jhbojsc401wezhmkzuyu','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753281002/jhbojsc401wezhmkzuyu.jpg'),(135,'xiaomi-redmi-note-14-6gb128gb-main-35826.png','tznd1zs2kqqkyyjalccv','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753281004/tznd1zs2kqqkyyjalccv.jpg'),(136,'Xiaomi-Redmi-Note-14-6GB-128GB-Tim.jpg','aqhrwdzwmjjkx5vzo3ha','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753281006/aqhrwdzwmjjkx5vzo3ha.jpg'),(137,'xiaomi-redmi-note-14-pro-plus-5g-purple-thumbnew-600x600.jpg','ftgsjzx0wrn34yz9ug5e','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753281008/ftgsjzx0wrn34yz9ug5e.jpg'),(138,'4671-45837_vivobook_x515_silver_bhs_ha5.jpg','e2wupje1ydu6yzkrrz15','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289305/e2wupje1ydu6yzkrrz15.jpg'),(139,'24398-asus-expertbook-b1-b1502cva-nj0050w-1.webp','igrxtvhzda07jpa7xgqz','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289307/igrxtvhzda07jpa7xgqz.webp'),(140,'41918_laptop_asus_vivobook_15_x1502za_bq127w_1_.jpg','dhg0p9o0w5qj73iwejuq','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289309/dhg0p9o0w5qj73iwejuq.jpg'),(141,'57002_laptop_asus_expertbook_p1_p1403cva_11.jpg','z7emytrdgiktrnlltj94','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289311/z7emytrdgiktrnlltj94.jpg'),(142,'10056000-laptop-asus-vivobook-14-oled-i5-13500h-16gb-512gb-win11-05va-km095w-1_i3cu-qw.webp','spxtamwl0li4awscwc2p','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289312/spxtamwl0li4awscwc2p.webp'),(143,'ve-sinh-laptop-asus-10.webp','jeclq4ptb2u7kddxbdkx','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289314/jeclq4ptb2u7kddxbdkx.webp'),(144,'7190_dell_3430__04_.jpg','fbtwoldqbepjjj1sx3zj','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289315/fbtwoldqbepjjj1sx3zj.jpg'),(145,'49221_laptop_dell_latitude_3540_71038100__1_.jpg','jjkjuhvgtvez7btuircf','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289317/jjkjuhvgtvez7btuircf.jpg'),(146,'dell-e7440-cu-1669436313_e3e9067ca332476fb20d07df6ff8761d.jpg','vohfk6ozpszcvqgswrxs','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289319/vohfk6ozpszcvqgswrxs.jpg'),(147,'Dell-Latitude-5400.jpg','x6wkbsdttvhsv7gford2','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289320/x6wkbsdttvhsv7gford2.jpg'),(148,'dell-pro-laptops-category-image-800x620.avif','uktxbyxbhzs1plqlrlox','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289322/uktxbyxbhzs1plqlrlox.avif'),(149,'ffe88d08-e669-4b5e-81b2-2249b2bbbb6e.300ac22db0e81d616b177694155a5eef.webp','oxt6offqo3nshysk54sv','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289324/oxt6offqo3nshysk54sv.webp'),(150,'53200-laptop-hp-probook-450-g10-4-1c758bcc-2787-45e7-a92f-8bd37db5ee39-a26ffc0c-2aec-4dfb-a9b9-4dec1e09737a-4217c037-6a52-4996-a196-8f6ebb885382.webp','frubtilvzagyuoafj4m6','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289326/frubtilvzagyuoafj4m6.jpg'),(151,'55753_laptop_hp_gaming_victus_16_r0216tx_9q973pa_6.jpg','u3qs3i0r6b7li1ev6bg6','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289328/u3qs3i0r6b7li1ev6bg6.jpg'),(152,'laptop-hp-15-fd0235tu-9q970pa-06.webp','ggdoldtj2ffywrgiovta','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289329/ggdoldtj2ffywrgiovta.webp'),(153,'media@2x1.avif','dvzeoks3yf1iojajouqo','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289331/dvzeoks3yf1iojajouqo.avif'),(154,'MT_HP_I3_14s-dq5122TU_8W356PA-1.jpg','ihukrq4shrackfx9ltkm','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289332/ihukrq4shrackfx9ltkm.jpg'),(155,'71bphKmt0DL.jpg','nlib8ajn7mikswpkdhia','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289335/nlib8ajn7mikswpkdhia.jpg'),(156,'7034_laptop_lenovo_thinkpad_t16_gen_1_21bv00g9fq__001_.jpg','oqo6m3rpp62eoouwgfqh','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289337/oqo6m3rpp62eoouwgfqh.jpg'),(157,'49670_laptop_lenovo_loq_15arp9_83jc007hvn__1_.jpg','jn8dygxrodyf3twdgplf','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289339/jn8dygxrodyf3twdgplf.jpg'),(158,'Laptop-Lenovo-Ideapad-3-15ITL05-nguyenvu.store-01.jpg','yukqxa0pvn6ozxr3fc0p','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289341/yukqxa0pvn6ozxr3fc0p.jpg'),(159,'laptop-lenovo-ideapad-slim-3-15irh8-83em003evn-3.webp','l8ertqajnpxsf9hkcrkr','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289343/l8ertqajnpxsf9hkcrkr.jpg'),(160,'lenovoideapad3gaming8-1.jpg','qxewez9k9w53z4sq4wzv','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289345/qxewez9k9w53z4sq4wzv.jpg'),(161,'lenovo-ideapad-slim-3-15irh10-i5-83k1000hvn-638775478046964172-600x600.jpg','bddhcfuonqnusswiymqz','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289347/bddhcfuonqnusswiymqz.jpg'),(162,'Laptop-MSI-Alpha-15-B5EEK-01.avif','zojdw295yrlhiuqrqg90','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289348/zojdw295yrlhiuqrqg90.avif'),(163,'laptop-msi-khong-len-nguon-2_2fQIFCW.jpg','bj0e2uragqaztvgslnls','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289349/bj0e2uragqaztvgslnls.jpg'),(164,'msi-gf65-10ser-03_f7448eadd4e64ec1b94c2faefc8a5f6d.jpg','qwsf8aerlbxlzhw5awx9','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289351/qwsf8aerlbxlzhw5awx9.jpg'),(165,'msi-gp65-leopard-144hz-600x600.jpg','vu5vvkf3dzopvfajs0eq','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289352/vu5vvkf3dzopvfajs0eq.jpg'),(166,'msi-thin-a15-cong-ket-noi-trai-66a3ce0c-2a99-47b0-9bb9-5871bca7993c-9340cc7a-c940-4f97-80e5-689e11658fda.webp','mgwfjy6mfqaigb2muc0f','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289354/mgwfjy6mfqaigb2muc0f.jpg'),(167,'unnamed.webp','synpdgqofqfl2uvyibil','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289355/synpdgqofqfl2uvyibil.webp'),(168,'ba-120spl-1adr_19c9b26698b04331bae26fd00fa4c4c1_1024x1024.png','paowtfkrfzqnlt6j0kxq','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289642/paowtfkrfzqnlt6j0kxq.png'),(169,'casio-a158wa-1df-bac-1-2-700x467.jpg','cxzb5dn3aghpapz7zfks','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289643/cxzb5dn3aghpapz7zfks.jpg'),(170,'casio-ae-1200wh-1avdf-den-nt-600x600.jpg','ccprzjefckmhrlfiq99i','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289645/ccprzjefckmhrlfiq99i.jpg'),(171,'casio-aeq-120w-2avdf-nam-1-750x500.jpg','dboixolpn22biyucdlh6','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289647/dboixolpn22biyucdlh6.jpg'),(172,'Casio-LTP-E117D-1AEF-646x783.jpg','uu9mnt0ajd5wx4zsnzsa','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289649/uu9mnt0ajd5wx4zsnzsa.jpg'),(173,'dong-ho-casio-dien-tu-day-vo-nhua-f94wa9dg-1586870121.jpg','ysxvfuamscfuxkvelmas','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289650/ysxvfuamscfuxkvelmas.jpg'),(174,'Dong-Ho-Nam-Unisex-Chinh-Hang-CASIO-F-91W-1.jpg','u5opyvbvuic4egmvjpeb','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289653/u5opyvbvuic4egmvjpeb.jpg'),(175,'edifice_casio_ca_efr_526l_7avudfjpg_1628060035-1725366677.jpg','yyrcicninzo5lt4bartn','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289654/yyrcicninzo5lt4bartn.jpg'),(176,'image_1-5.webp','ufzmqxqrdave2dnbknvf','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289656/ufzmqxqrdave2dnbknvf.webp'),(177,'images.jpeg','avfprn7c9hnwrfbvyvaq','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289657/avfprn7c9hnwrfbvyvaq.jpg'),(178,'MTP-E720D-8AVDF.jpg','fdyzmqnooigerzdsylbi','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289659/fdyzmqnooigerzdsylbi.jpg'),(179,'ws-b1000-1av_front_e569d19cafac4bf4b25a1b84624a2f48_master.png','i5pv10vk02zsryidyiva','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289662/i5pv10vk02zsryidyiva.png'),(180,'510.OX_.1180.OX_.jpg','ma0dpknkiv0ijajp4alu','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289664/ma0dpknkiv0ijajp4alu.jpg'),(181,'Capture-34.png','hn1dzvsg71aq6wxdyojb','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289667/hn1dzvsg71aq6wxdyojb.png'),(182,'dong-ho-hublot-big-bang-sang-bleu-ii-titanium-blue-45mm-418-nx-5107-rx-mxm20-2.jpg','bqs3ukmkgtiyl6zklvin','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289669/bqs3ukmkgtiyl6zklvin.jpg'),(183,'dong-ho-hublot-classic-fusion-black-magic-581-cm-1171-rx.jpg','reymuasovnysbibzicf4','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289671/reymuasovnysbibzicf4.jpg'),(184,'dong-ho-hublot-classic-fusion-orlinski-titanium-40mm-550-ns-1800-rx-orl19-1.jpg','bolq6je869pkjk1i0eq7','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289673/bolq6je869pkjk1i0eq7.jpg'),(185,'dong-ho-hublot-nam-chinh-hang-511-ox-7180-lr.jpg','picnqon8fhawj9gakald','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289675/picnqon8fhawj9gakald.jpg'),(186,'dong-ho-hublot-spirit-of-big-bang-chronograph-automatic-mens-watch-642-ox-7180-rx.jpg','phzl5yxdm9yu1hcfq1ls','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289676/phzl5yxdm9yu1hcfq1ls.jpg'),(187,'hublot-542-nx-1171-rx-classic-fusion-mens-watch-42mm.jpg_980_980.webp','nuclu2wm2uysmzbsdafw','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289678/nuclu2wm2uysmzbsdafw.webp'),(188,'hublot-classic-fusion-542-no-1181-lr-men-s-watch-42mm.jpg_980_980.webp','iiruj46y1lkooahjvgr8','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289680/iiruj46y1lkooahjvgr8.webp'),(189,'images.jpeg','hrch8asvnxdsbqh6icsp','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289681/hrch8asvnxdsbqh6icsp.jpg'),(190,'a0.jpg','pvumvlkobjchrzkaaisb','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289683/pvumvlkobjchrzkaaisb.jpg'),(191,'Dồng-hồ-rolex-6.png','dfsrfcot713uwzb5j92p','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289686/dfsrfcot713uwzb5j92p.png'),(192,'dong-ho-rolex-datejust-31-278273-0032-mat-so-hoa-tiet-hoa-xanh-olive-day-deo-jubilee-thep-vang-vang-4-1.jpg','y6btjjnu3oeuwpibd4dg','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289689/y6btjjnu3oeuwpibd4dg.jpg'),(193,'dong-ho-rolex-datejust-36-126200-0001-mat-so-bac-day-deo-jubilee-8.jpg','qtf8qi5w3xbdbbf18fba','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289691/qtf8qi5w3xbdbbf18fba.jpg'),(194,'dong-ho-rolex-datejust-36-dial-blue-benzel-diamond-126284rbr-mau-bac-65ab4c5ae2100-20012024113018.webp','ydmpeszrrxkuvfo3n7s8','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289692/ydmpeszrrxkuvfo3n7s8.webp'),(195,'images.jpeg','auqxrofxhnd0zfdu8pdm','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289694/auqxrofxhnd0zfdu8pdm.jpg'),(196,'m126201-0043result.jpg','sizoslf1911sdzzrqqfe','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289697/sizoslf1911sdzzrqqfe.jpg'),(197,'rolex-oyster-perpetual-datejust-116233-5469.jpg','pwzjyfibdzjghbczjlkc','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289699/pwzjyfibdzjghbczjlkc.jpg'),(198,'Rolex-Submariner-Date-Oyster-steel-and-yellow-gold-116613ln-0001-40-mm.jpg','jaanbvt4ujt45pd4sndl','https://res.cloudinary.com/da4p9sh2j/image/upload/v1753289702/jaanbvt4ujt45pd4sndl.jpg'),(199,'4bd59709b71601485807.jpg','l9jld5dk1y9zknnjpahl','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754830002/l9jld5dk1y9zknnjpahl.jpg'),(200,'2ac71d0ebeab08f551ba.jpg','qrb0h8m6q6vm7isz7qsp','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754830808/qrb0h8m6q6vm7isz7qsp.jpg'),(201,'bdabfbedc794bb1cf6e88c06ac8dda307b594a34_high.webp','kt7mzbf8wjyai5izcmdi','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754831150/kt7mzbf8wjyai5izcmdi.webp'),(202,'aca783e98e0a3854611b.jpg','lpgrvqagktkdss7xhxg7','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754831201/lpgrvqagktkdss7xhxg7.jpg'),(203,'chill15.jpg','zdfbdwrjlv9xw6ik0pxo','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754831204/zdfbdwrjlv9xw6ik0pxo.jpg'),(204,'chill16.jpg','g6fyyfcbmzqvzwebve5k','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754831207/g6fyyfcbmzqvzwebve5k.jpg'),(205,'chú mèo dưới hoa anh đào.png','j3gju2hykdm6jnymx3d2','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754831224/j3gju2hykdm6jnymx3d2.png'),(206,'2ac71d0ebeab08f551ba.jpg','a2qrk7aaqcarfyj4yhg8','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754831364/a2qrk7aaqcarfyj4yhg8.jpg'),(207,'chill15.jpg','ctn9frwcqhpxcebp1gqc','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754831551/ctn9frwcqhpxcebp1gqc.jpg'),(208,'chill15.jpg','mmx103qg6dycnyzxihkx','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754832024/mmx103qg6dycnyzxihkx.jpg'),(209,'aca783e98e0a3854611b.jpg','bqfxahe0xuzxceqjz7qp','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754832255/bqfxahe0xuzxceqjz7qp.jpg'),(210,'4bd59709b71601485807.jpg','m7fgju8tqouhu4vd73ws','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754832487/m7fgju8tqouhu4vd73ws.jpg'),(211,'4bd59709b71601485807.jpg','jly2ycolxs1quyhwysfu','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754833502/jly2ycolxs1quyhwysfu.jpg'),(212,'aca783e98e0a3854611b.jpg','dne1mnvfl2zpw3usvqwg','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754835249/dne1mnvfl2zpw3usvqwg.jpg'),(213,'chill15.jpg','vulqrr00o49jrevx9tc2','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754835266/vulqrr00o49jrevx9tc2.jpg'),(214,'aca783e98e0a3854611b.jpg','smanf6yotjfc6u3oowet','https://res.cloudinary.com/da4p9sh2j/image/upload/v1754835403/smanf6yotjfc6u3oowet.jpg');
/*!40000 ALTER TABLE `upload_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_vouchers`
--

DROP TABLE IF EXISTS `user_vouchers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_vouchers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `assigned_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_used` bit(1) NOT NULL,
  `user_id` int DEFAULT NULL,
  `voucher_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK90ahc2var0yrghyxr9tapdokg` (`user_id`),
  KEY `FK40ig7khk2v79rbqaj98mf1g2q` (`voucher_id`),
  CONSTRAINT `FK40ig7khk2v79rbqaj98mf1g2q` FOREIGN KEY (`voucher_id`) REFERENCES `vouchers` (`id`),
  CONSTRAINT `FK90ahc2var0yrghyxr9tapdokg` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_vouchers`
--

LOCK TABLES `user_vouchers` WRITE;
/*!40000 ALTER TABLE `user_vouchers` DISABLE KEYS */;
INSERT INTO `user_vouchers` VALUES (1,'2025-08-09 15:14:11',_binary '',4,1),(2,'2025-08-09 21:44:57',_binary '',4,3),(3,'2025-08-10 10:14:37',_binary '',9,4);
/*!40000 ALTER TABLE `user_vouchers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,NULL,NULL,'admin@gmail.com',NULL,'$2a$10$IJ9YIZGGFJNnAfjhMe5K8u2rGzwcJPoX/iZ5m4K9hUTzfqAIpFB9i',NULL,NULL),(2,'2025-07-24 13:32:55.707517',NULL,'tienstaff@gmail.com','tien','$2a$10$rJr1GZj1l/4ApLuXvOb11eW1c1KUh5BjORi8OA13T22BJ/inRbamO','0385742764','2025-08-09 04:24:38.509027'),(3,'2025-07-24 22:56:35.931310',NULL,'tien2@gmail.com','tien','$2a$10$eV/HbDmCVPUldzNpv.A64OBNEYMurcw/0qu..n1BVEUQXbiCxzfzu','0123456789','2025-07-25 16:13:44.612118'),(4,'2025-07-25 10:58:51.338286',NULL,'tien3@gmail.com','tien34','$2a$10$lI9Y87ztNOfSCDC9FwX3YOP0YztJB8hnba/ZCv3ZBOZvtTULMFMkq','0123456565','2025-08-10 21:34:29.520541'),(5,'2025-07-25 20:46:08.542858',NULL,'tien@gmail.com','tien','$2a$10$fqG6JJp3PUaxPc2ukZZETOpuy009RLSKvamUqAYhhqFlFdyxvz5lS','0123464646','2025-08-08 17:40:55.253439'),(6,'2025-07-31 19:42:01.796495',NULL,'tienmanager@gmail.com','tien manager','$2a$10$s6ybINZ1mwLzIdycD.g0tesajsJ6Ldf2EaAJoBgtaqJ3/MiXIkkVK','0123454546','2025-08-10 13:12:44.755941'),(7,'2025-08-09 04:07:10.458247',NULL,'tienvtps40685@gmail.com','tien','$2a$10$yVp/K1GlPfjBMhu7llE7J.KbUnUzrsqYd.l5YcLwcPPJLRsocPzbi','0912343434','2025-08-10 22:28:57.507583'),(8,'2025-08-09 04:21:12.790328',NULL,'test@gmail.com','tien','$2a$10$7HSYKCaDX6VZwhF5MQWEBeWtbTDyAEEK.DIzMC3VEkQ41/qF/rUK2','0123464546','2025-08-09 04:21:12.790359'),(9,'2025-08-10 09:51:31.386570',NULL,'tientest@gmail.com','tien test giao diẹn','$2a$10$isq6oKyRc.QCNoIS5TFUeOLJWDWTFpg//e4OZDUCgpP5fnhXSgxsC','0123545454','2025-08-10 09:51:31.386602'),(10,'2025-08-10 13:11:01.881377',NULL,'test23@gmail.com','test','$2a$10$2U3ieCiyCekbZvLFdIkj9.Y1oRzCCsG0xsbEWISvX9HVA1DJPe.Nu','0123545467','2025-08-10 13:11:01.881460');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_roles` (
  `user_id` int NOT NULL,
  `roles_name` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`,`roles_name`),
  KEY `FKmi9sfx618v14gm89cyw408hqu` (`roles_name`),
  CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKmi9sfx618v14gm89cyw408hqu` FOREIGN KEY (`roles_name`) REFERENCES `roles` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_roles`
--

LOCK TABLES `users_roles` WRITE;
/*!40000 ALTER TABLE `users_roles` DISABLE KEYS */;
INSERT INTO `users_roles` VALUES (1,'ADMIN'),(3,'CUSTOMER'),(4,'CUSTOMER'),(5,'CUSTOMER'),(7,'CUSTOMER'),(8,'CUSTOMER'),(9,'CUSTOMER'),(10,'CUSTOMER'),(1,'MANAGER'),(6,'MANAGER'),(2,'SHIFT_STAFF');
/*!40000 ALTER TABLE `users_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `variant_attribute_values`
--

DROP TABLE IF EXISTS `variant_attribute_values`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `variant_attribute_values` (
  `id` int NOT NULL AUTO_INCREMENT,
  `value` varchar(100) DEFAULT NULL,
  `attribute_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlrch22xkutclfryhwunfq11oj` (`attribute_id`),
  CONSTRAINT `FKlrch22xkutclfryhwunfq11oj` FOREIGN KEY (`attribute_id`) REFERENCES `variant_attributes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `variant_attribute_values`
--

LOCK TABLES `variant_attribute_values` WRITE;
/*!40000 ALTER TABLE `variant_attribute_values` DISABLE KEYS */;
INSERT INTO `variant_attribute_values` VALUES (1,'16GB',1),(2,'32GB',1),(3,'64GB',2),(4,'128GB',2),(5,'256GB',2),(6,'Xanh',3),(7,'Đỏ',3),(8,'Vàng',3),(9,'Titan',3);
/*!40000 ALTER TABLE `variant_attribute_values` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `variant_attributes`
--

DROP TABLE IF EXISTS `variant_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `variant_attributes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `variant_attributes`
--

LOCK TABLES `variant_attributes` WRITE;
/*!40000 ALTER TABLE `variant_attributes` DISABLE KEYS */;
INSERT INTO `variant_attributes` VALUES (1,'Ram'),(2,'Rom'),(3,'Màu sắc');
/*!40000 ALTER TABLE `variant_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vouchers`
--

DROP TABLE IF EXISTS `vouchers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vouchers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `discount_value` decimal(10,2) DEFAULT NULL,
  `end_at` datetime(6) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `min_order_value` decimal(10,2) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `start_at` datetime(6) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `usage_count` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_30ftp2biebbvpik8e49wlmady` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vouchers`
--

LOCK TABLES `vouchers` WRITE;
/*!40000 ALTER TABLE `vouchers` DISABLE KEYS */;
INSERT INTO `vouchers` VALUES (1,'CHAOMUNG','2025-07-25 17:32:07.854695','Chào mừng thành viên mới',1.00,'2025-08-16 23:59:59.000000',_binary '',100000.00,1000000,'2025-07-25 00:00:00.000000',NULL,'2025-08-10 10:07:32.137294',4),(2,'CHAOMUNG2','2025-07-25 17:32:42.470804','CHAOMUNG2',100000.00,'2025-08-16 23:59:59.000000',_binary '',100000.00,100000,'2025-07-11 00:00:00.000000',NULL,'2025-08-07 22:15:16.898064',0),(3,'CHAOMUNG3','2025-07-25 17:32:07.854695','Chào mừng thành viên mới',1.00,'2025-08-16 23:59:59.000000',_binary '',100000.00,1000000,'2025-07-25 00:00:00.000000',NULL,'2025-08-10 10:07:32.137294',5),(4,'CHAOMUNG4','2025-07-25 17:32:07.854695','Chào mừng thành viên mới',1.00,'2025-08-16 23:59:59.000000',_binary '',100000.00,100000000,'2025-07-24 00:00:00.000000',NULL,'2025-08-10 10:07:59.045497',1);
/*!40000 ALTER TABLE `vouchers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'datn'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-10 23:37:23
