SET SQL_LOG_BIN=0;
SET @@SESSION.UNIQUE_CHECKS=0;
SET @@SESSION.FOREIGN_KEY_CHECKS=0;
SET @@SESSION.SQL_MODE='NO_ENGINE_SUBSTITUTION,ALLOW_INVALID_DATES';
DROP SCHEMA IF EXISTS `vws` ;
CREATE SCHEMA IF NOT EXISTS `vws` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;

--
-- Not dumping tablespaces as no INFORMATION_SCHEMA.FILES table on this server
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `vws` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `vws`;

DROP TABLE IF EXISTS `favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `favorite` (
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;