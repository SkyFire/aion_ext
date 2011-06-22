SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

CREATE TABLE IF NOT EXISTS `ingameshop` (
  `object_id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) NOT NULL,
  `item_count` int(11) NOT NULL DEFAULT '0',
  `item_price` int(11) NOT NULL DEFAULT '0',
  `category` int(11) NOT NULL DEFAULT '0',
  `list` int(11) NOT NULL DEFAULT '0',
  `sales_ranking` int(11) NOT NULL DEFAULT '1',
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`object_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=80522 ;

INSERT INTO `ingameshop` (`object_id`, `item_id`, `item_count`, `item_price`, `category`, `list`, `sales_ranking`, `description`) VALUES
(80435, 112100331, 1, 20, 6, 0, 1, 'Test1'),
(80436, 112100331, 1, 20, 6, 0, 1, 'Test2'),
(80437, 112100331, 1, 20, 6, 0, 1, 'Test3'),
(80438, 112100331, 1, 20, 6, 0, 1, 'Test4'),
(80439, 112100331, 1, 20, 6, 0, 1, 'Test5'),
(80440, 112100331, 1, 20, 6, 0, 1, 'Test6'),
(80441, 112100331, 1, 20, 6, 0, 1, 'Test7'),
(80442, 112100331, 1, 21, 6, 0, 1, 'Test8'),
(80443, 112100331, 1, 21, 6, 0, 1, 'Test9'),
(80445, 112100331, 1, 22, 6, 1, 1, 'Test10'),
(80446, 112100331, 1, 23, 6, 1, 1, 'Test11'),
(80448, 100500828, 1, 50, -1, -1, 0, 'Kaligas'),
(80449, 120000596, 1, 50, -1, -1, 0, 'Lapi'),
(80506, 120000596, 1, 1, 4, 0, 1, 'Test4'),
(80509, 120000596, 1, 1, 4, 0, 1, 'Test4'),
(80511, 120000596, 1, 1, 4, 0, 1, 'Test4');

CREATE TABLE IF NOT EXISTS `ingameshopcategorys` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

INSERT INTO `ingameshopcategorys` (`id`, `name`) VALUES
(3, 'Weapons'),
(4, 'Consumables'),
(5, 'Misc. Items'),
(6, 'Armor'),
(7, 'Weapon Modifications'),
(8, 'Armor Modifications');

CREATE TABLE IF NOT EXISTS `ingameshoplog` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `account` varchar(255) NOT NULL,
  `character` varchar(255) NOT NULL,
  `receiver` varchar(255) NOT NULL,
  `item` int(11) NOT NULL,
  `count` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;