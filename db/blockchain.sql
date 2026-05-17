-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.5.62 - MySQL Community Server (GPL)
-- Server OS:                    Win32
-- HeidiSQL Version:             10.3.0.5771
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for blockchaincloud
CREATE DATABASE IF NOT EXISTS `blockchaincloud` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `blockchaincloud`;

-- Dumping structure for table blockchaincloud.register
CREATE TABLE IF NOT EXISTS `register` (
  `uname` varchar(50) DEFAULT NULL,
  `pass` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table blockchaincloud.register: ~2 rows (approximately)
DELETE FROM `register`;
/*!40000 ALTER TABLE `register` DISABLE KEYS */;
INSERT INTO `register` (`uname`, `pass`, `email`) VALUES
	('aaa', 'aaa', 'aaa@gmail.com'),
	('bbb', 'bbb', 'bbb@gmail.com');
/*!40000 ALTER TABLE `register` ENABLE KEYS */;

-- Dumping structure for table blockchaincloud.share
CREATE TABLE IF NOT EXISTS `share` (
  `uname` varchar(50) DEFAULT NULL,
  `rname` varchar(50) DEFAULT NULL,
  `fname` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table blockchaincloud.share: ~1 rows (approximately)
DELETE FROM `share`;
/*!40000 ALTER TABLE `share` DISABLE KEYS */;
INSERT INTO `share` (`uname`, `rname`, `fname`) VALUES
	('aaa', 'bbb', 'ipaddr.java');
/*!40000 ALTER TABLE `share` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
