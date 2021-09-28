CREATE DATABASE `golem` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE TABLE `provider` (
  `id` varchar(42) NOT NULL,
  `pname` varchar(128) DEFAULT NULL,
  `created_at` varchar(100) DEFAULT NULL,
  `updated_at` varchar(100) DEFAULT NULL,
  `cores` int DEFAULT NULL,
  `threads` int DEFAULT NULL,
  `mem_gb` double DEFAULT NULL,
  `c1` double DEFAULT NULL,
  `c2` double DEFAULT NULL,
  `c3` double DEFAULT NULL,
  `is_online` int DEFAULT NULL,
  `network` varchar(50) DEFAULT NULL,
  `bench1` double DEFAULT NULL,
  `bench2` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `results` (
  `provider` varchar(128) DEFAULT NULL,
  `bench1` double DEFAULT NULL,
  `bench2` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
