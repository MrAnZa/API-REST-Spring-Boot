-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 16-08-2019 a las 22:35:01
-- Versión del servidor: 5.7.26
-- Versión de PHP: 7.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `platziprofesores`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `course`
--

DROP TABLE IF EXISTS `course`;
CREATE TABLE IF NOT EXISTS `course` (
  `id_course` int(11) NOT NULL AUTO_INCREMENT,
  `id_teacher` int(11) DEFAULT NULL,
  `name` varchar(250) NOT NULL,
  `themes` text,
  `project` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id_course`),
  KEY `id_teacher` (`id_teacher`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `course`
--

INSERT INTO `course` (`id_course`, `id_teacher`, `name`, `themes`, `project`) VALUES
(1, 1, 'Java Advanced', 'Tema 1', 'Rest API'),
(2, NULL, 'Python Basico', 'Python Language', ''),
(4, NULL, 'PHP', 'De Basico a Experto', 'InventariAPP');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `social_media`
--

DROP TABLE IF EXISTS `social_media`;
CREATE TABLE IF NOT EXISTS `social_media` (
  `id_social_media` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL,
  `icon` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id_social_media`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `social_media`
--

INSERT INTO `social_media` (`id_social_media`, `name`, `icon`) VALUES
(1, 'twitter', ''),
(2, 'facebook', 'avatar'),
(4, 'Instagram', 'images/socialmedias/4-pictureSocialMedia-2019-08-16-11-05-16.png');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `teacher`
--

DROP TABLE IF EXISTS `teacher`;
CREATE TABLE IF NOT EXISTS `teacher` (
  `id_teacher` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL,
  `avatar` varchar(250) NOT NULL,
  PRIMARY KEY (`id_teacher`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `teacher`
--

INSERT INTO `teacher` (`id_teacher`, `name`, `avatar`) VALUES
(1, 'andrés Zamora', 'images/teachers/1-pictureTeacher-2019-08-15-17-41-34.png'),
(2, 'Freddy', ''),
(3, 'Cristian', 'images/teachers/3-pictureTeacher-2019-08-16-08-58-39.jpeg');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `teacher_social_media`
--

DROP TABLE IF EXISTS `teacher_social_media`;
CREATE TABLE IF NOT EXISTS `teacher_social_media` (
  `id_teacher_social_media` int(11) NOT NULL AUTO_INCREMENT,
  `id_teacher` int(11) NOT NULL,
  `id_social_media` int(11) NOT NULL,
  `nickname` varchar(250) NOT NULL,
  PRIMARY KEY (`id_teacher_social_media`),
  KEY `id_teacher` (`id_teacher`,`id_social_media`),
  KEY `fk_social_social_media` (`id_social_media`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `course`
--
ALTER TABLE `course`
  ADD CONSTRAINT `fk_id_teacher` FOREIGN KEY (`id_teacher`) REFERENCES `teacher` (`id_teacher`);

--
-- Filtros para la tabla `teacher_social_media`
--
ALTER TABLE `teacher_social_media`
  ADD CONSTRAINT `fk_id_teacher_social_media` FOREIGN KEY (`id_teacher`) REFERENCES `teacher` (`id_teacher`),
  ADD CONSTRAINT `fk_social_social_media` FOREIGN KEY (`id_social_media`) REFERENCES `social_media` (`id_social_media`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
