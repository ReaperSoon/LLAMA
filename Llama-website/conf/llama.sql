# --- !Ups

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `llama`
--
CREATE DATABASE IF NOT EXISTS `llama` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `llama`;

-- --------------------------------------------------------

--
-- Structure de la table `bill`
--

CREATE TABLE IF NOT EXISTS `bill` (
  `bill_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `shipment_address` varchar(255) NOT NULL,
  UNIQUE KEY `bill_id` (`bill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `bill_line`
--

CREATE TABLE IF NOT EXISTS `bill_line` (
  `bill_line_id` int(11) NOT NULL AUTO_INCREMENT,
  `bill_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_number` int(11) NOT NULL,
  PRIMARY KEY (`bill_line_id`),
  KEY `bill_id` (`bill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(56) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Contenu de la table `category`
--

INSERT INTO `category` (`category_id`, `name`, `description`) VALUES
(1, 'plush', 'plush with supra quality');

-- --------------------------------------------------------

--
-- Structure de la table `product`
--

CREATE TABLE IF NOT EXISTS `product` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(56) NOT NULL,
  `description` text NOT NULL,
  `price` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `rating` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Contenu de la table `product`
--

INSERT INTO `product` (`product_id`, `name`, `description`, `price`, `quantity`, `rating`) VALUES
(1, 'White plush Llama', 'A white plush Llama\r\n40cm\r\nfor +3 years olrd', 200, 20, 0),
(2, 'Black plush Llama', 'A black plush Llama\r\n40cm\r\nfor +3 years olrd', 200, 20, 0),
(3, 'Raimbow plush Llama', 'A Rainbow plush Llama\r\n40cm\r\nfor +3 years olrd', 200, 20, 0);

-- --------------------------------------------------------

--
-- Structure de la table `product_category`
--

CREATE TABLE IF NOT EXISTS `product_category` (
  `product_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `product_category`
--

INSERT INTO `product_category` (`product_id`, `category_id`) VALUES
(1, 1),
(2, 1),
(3, 1);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(56) NOT NULL,
  `password` varchar(56) NOT NULL,
  `last_name` varchar(56) NOT NULL,
  `first_name` varchar(56) NOT NULL,
  `email_address` varchar(56) NOT NULL,
  `phone_number` int(11) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_address` (`email_address`),
  KEY `login` (`login`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Contenu de la table `user`
--

INSERT INTO `user` (`user_id`, `login`, `password`, `last_name`, `first_name`, `email_address`, `phone_number`) VALUES
(1, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Admin', 'Admin', 'myadressemail@gmail.com', 1234567891),
(2, 'loveLlama', '4576620de10e9c48275167fb9aa338ca', 'Dupond', 'Francois', 'lovellama@gmail.com', 123456789),
(4, 'Swag182xx-xx', '4576620de10e9c48275167fb9aa338ca', 'Du con', 'Gilbert', 'lol@gmail.com', 123456789);

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `bill`
--
ALTER TABLE `bill`
  ADD CONSTRAINT `bill_ibfk_1` FOREIGN KEY (`bill_id`) REFERENCES `bill_line` (`bill_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
