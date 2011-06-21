/*
Navicat MySQL Data Transfer

Source Server         : Aion
Source Server Version : 50508
Source Host           : localhost:3316
Source Database       : aionx_ls

Target Server Type    : MYSQL
Target Server Version : 50508
File Encoding         : 65001

Date: 2011-06-09 12:01:10
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `account_data`
-- ----------------------------
DROP TABLE IF EXISTS `account_data`;
CREATE TABLE `account_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `password` varchar(65) NOT NULL,
  `activated` tinyint(1) NOT NULL DEFAULT '1',
  `access_level` tinyint(3) NOT NULL DEFAULT '0',
  `membership` tinyint(3) NOT NULL DEFAULT '0',
  `last_server` tinyint(3) NOT NULL DEFAULT '-1',
  `last_ip` varchar(20) DEFAULT NULL,
  `ip_force` varchar(20) DEFAULT NULL,
  `credits` bigint(21) NOT NULL DEFAULT '10000',
  `expire` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account_data
-- ----------------------------
INSERT INTO account_data VALUES ('1', 'admin', 'kALDYHyMGoEFCt3OeSo/ka25blY=', '1', '0', '0', '1', '192.168.2.101', null, '10000', null);
INSERT INTO account_data VALUES ('2', 'evillcreed', 'kALDYHyMGoEFCt3OeSo/ka25blY=', '1', '0', '0', '1', '192.168.2.101', null, '9550', null);
