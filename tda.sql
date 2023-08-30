-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Aug 06, 2023 at 06:52 AM
-- Server version: 5.7.24
-- PHP Version: 7.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tda`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_devices`
--

DROP TABLE IF EXISTS `tbl_devices`;
CREATE TABLE IF NOT EXISTS `tbl_devices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_persian_ci DEFAULT NULL,
  `bl_name` varchar(255) COLLATE utf8_persian_ci DEFAULT NULL,
  `ip` varchar(255) COLLATE utf8_persian_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  `password` varchar(20) COLLATE utf8_persian_ci DEFAULT NULL,
  `fp_password` varchar(20) COLLATE utf8_persian_ci DEFAULT NULL,
  `is_deleted` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk2` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_persian_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_device_details`
--

DROP TABLE IF EXISTS `tbl_device_details`;
CREATE TABLE IF NOT EXISTS `tbl_device_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_persian_ci DEFAULT NULL,
  `type` varchar(50) COLLATE utf8_persian_ci DEFAULT NULL,
  `fp_user_id` varchar(20) COLLATE utf8_persian_ci NOT NULL,
  `is_deleted` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk1` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_persian_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_products`
--

DROP TABLE IF EXISTS `tbl_products`;
CREATE TABLE IF NOT EXISTS `tbl_products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_persian_ci NOT NULL,
  `serial_number` varchar(10) COLLATE utf8_persian_ci NOT NULL,
  `owner_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk3` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_persian_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_users`
--

DROP TABLE IF EXISTS `tbl_users`;
CREATE TABLE IF NOT EXISTS `tbl_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_persian_ci DEFAULT NULL,
  `mobile` varchar(11) COLLATE utf8_persian_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_persian_ci NOT NULL,
  `update_at` timestamp NULL DEFAULT NULL,
  `activation` varchar(6) COLLATE utf8_persian_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_persian_ci;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tbl_devices`
--
ALTER TABLE `tbl_devices`
  ADD CONSTRAINT `fk2` FOREIGN KEY (`user_id`) REFERENCES `tbl_users` (`id`);

--
-- Constraints for table `tbl_device_details`
--
ALTER TABLE `tbl_device_details`
  ADD CONSTRAINT `fk1` FOREIGN KEY (`device_id`) REFERENCES `tbl_devices` (`id`);

--
-- Constraints for table `tbl_products`
--
ALTER TABLE `tbl_products`
  ADD CONSTRAINT `fk3` FOREIGN KEY (`owner_id`) REFERENCES `tbl_users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
