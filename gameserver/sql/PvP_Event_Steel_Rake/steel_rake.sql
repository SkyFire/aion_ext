/*
Navicat MySQL Data Transfer

Source Server         : AE_Server25
Source Server Version : 50141
Source Host           : localhost:3306
Source Database       : au_server_gs
PvP Event Steel Rake  : @author Dallas Aion Extreme 2.5

Target Server Type    : MYSQL
Target Server Version : 50141
File Encoding         : 65001

Date: 2011-07-30 18:05:41
*/


--
-- PVP Steel Rake `droplist`
--

CREATE TABLE IF NOT EXISTS `droplist` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `mobId` int(11) NOT NULL DEFAULT '0',
  `itemId` int(11) NOT NULL DEFAULT '0',
  `min` int(11) NOT NULL DEFAULT '0',
  `max` int(11) NOT NULL DEFAULT '0',
  `chance` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=155281 ;

--
-- Fist Boss *Divine Crusader I* `droplist`
--

INSERT INTO `droplist` (`Id`, `mobId`, `itemId`, `min`, `max`, `chance`) VALUES
(155252, 281443, 185000037, 1, 1, 100),
(155254, 281443, 186000098, 1, 1, 100),
(155255, 281443, 186000098, 1, 1, 100),
(155256, 281443, 186000098, 1, 1, 100),
(155257, 281443, 186000098, 1, 1, 100),
(155258, 281443, 186000098, 1, 1, 100),
(155259, 281443, 186000098, 1, 1, 100),
(155281, 281443, 186000098, 1, 1, 100),
(155282, 281443, 186000098, 1, 1, 100),
(155283, 281443, 186000098, 1, 1, 100),
(155284, 281443, 186000098, 1, 1, 100),
(155285, 281443, 186000098, 1, 1, 100),
(155286, 281443, 186000098, 1, 1, 100),
(155287, 281443, 162002017, 1, 1, 100),
(155288, 281443, 162002017, 1, 1, 100),
(155289, 281443, 162002017, 1, 1, 100),
(155290, 281443, 162002017, 1, 1, 100),
(155291, 281443, 162002017, 1, 1, 100),
(155292, 281443, 162002017, 1, 1, 100),
(155293, 281443, 162002017, 1, 1, 100),
(155294, 281443, 162002017, 1, 1, 100),
(155295, 281443, 162002017, 1, 1, 100),
(155296, 281443, 162002017, 1, 1, 100),
(155297, 281443, 162002017, 1, 1, 100),
(155298, 281443, 162002017, 1, 1, 100);


--
-- Final Boss *Naduka Blade Captain* `droplist`
--

INSERT INTO `droplist` (`Id`, `mobId`, `itemId`, `min`, `max`, `chance`) VALUES
(155260, 281938, 186000098, 1, 1, 100),
(155262, 281938, 100900711, 1, 1, 50),
(155265, 281938, 101700749, 1, 1, 50),
(155267, 281938, 100500725, 1, 1, 50),
(155268, 281938, 101300681, 1, 1, 50),
(155269, 281938, 101500726, 1, 1, 50),
(155270, 281938, 100600782, 1, 1, 50),
(155280, 281938, 100000934, 1, 1, 50),
(155276, 281938, 100200834, 1, 1, 50);

