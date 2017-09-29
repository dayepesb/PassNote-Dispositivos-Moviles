-- phpMyAdmin SQL Dump
-- version 4.8.0-dev
-- https://www.phpmyadmin.net/
--
-- Servidor: 192.168.30.23
-- Tiempo de generación: 26-08-2017 a las 10:28:22
-- Versión del servidor: 8.0.0-dmr
-- Versión de PHP: 7.0.19-1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `PassNote`
--
CREATE SCHEMA IF NOT EXISTS `passnote` DEFAULT CHARACTER SET utf8 ;
USE `passnote` ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `accounts`
--

CREATE TABLE `accounts` (
  `idUser` int(11) NOT NULL,
  `type` varchar(32) COLLATE utf8_spanish_ci NOT NULL,
  `url` int(255) DEFAULT NULL,
  `user` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Notes`
--

CREATE TABLE `Notes` (
  `iduser` int(11) NOT NULL,
  `idNote` int(11) NOT NULL,
  `type` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `text` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `dateOfCreation` varchar(12) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `numberaccounts`
--

CREATE TABLE `numberaccounts` (
  `idUser` int(11) NOT NULL,
  `accounts` int(20) NOT NULL,
  `accountsAllowed` int(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `token`
--

CREATE TABLE `token` (
  `idUser` int(11) NOT NULL,
  `token` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `tokenDate` varchar(12) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `idUser` int(11) NOT NULL,
  `idCountry` varchar(24) COLLATE utf8_spanish_ci NOT NULL,
  `names` varchar(32) COLLATE utf8_spanish_ci NOT NULL,
  `surnames` varchar(32) COLLATE utf8_spanish_ci NOT NULL,
  `mail` varchar(32) COLLATE utf8_spanish_ci NOT NULL,
  `user` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `registrationDate` varchar(12) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`idUser`,`type`,`user`);

--
-- Indices de la tabla `Notes`
--
ALTER TABLE `Notes`
  ADD PRIMARY KEY (`idNote`),
  ADD UNIQUE KEY `idNote` (`idNote`);

--
-- Indices de la tabla `numberaccounts`
--
ALTER TABLE `numberaccounts`
  ADD PRIMARY KEY (`idUser`),
  ADD UNIQUE KEY `idUser` (`idUser`);

--
-- Indices de la tabla `token`
--
ALTER TABLE `token`
  ADD PRIMARY KEY (`idUser`),
  ADD KEY `idUser` (`idUser`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`idUser`),
  ADD UNIQUE KEY `idUser` (`idUser`),
  ADD UNIQUE KEY `idUser_2` (`idUser`),
  ADD UNIQUE KEY `idUser_3` (`idUser`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `Notes`
--
ALTER TABLE `Notes`
  MODIFY `idNote` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
