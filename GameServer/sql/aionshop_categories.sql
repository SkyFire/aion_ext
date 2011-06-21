/*
Navicat MySQL Data Transfer

Source Server         : Aion
Source Server Version : 50508
Source Host           : localhost:3316
Source Database       : aionx_gs

Target Server Type    : MYSQL
Target Server Version : 50508
File Encoding         : 65001

Date: 2011-06-09 12:01:20
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `aionshop_categories`
-- ----------------------------
DROP TABLE IF EXISTS `aionshop_categories`;
CREATE TABLE `aionshop_categories` (
  `categoryId` int(11) NOT NULL,
  `categoryName` varchar(255) NOT NULL,
  PRIMARY KEY (`categoryId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aionshop_categories
-- ----------------------------
INSERT INTO aionshop_categories VALUES ('3', 'Clothing');
INSERT INTO aionshop_categories VALUES ('4', 'Pet');
INSERT INTO aionshop_categories VALUES ('5', 'Food');
INSERT INTO aionshop_categories VALUES ('6', 'drinks');
INSERT INTO aionshop_categories VALUES ('10', 'Armor');
INSERT INTO aionshop_categories VALUES ('9', 'Weapon');
INSERT INTO aionshop_categories VALUES ('7', 'Wings');
INSERT INTO aionshop_categories VALUES ('8', 'Enchantment');

-- ----------------------------
-- Table structure for `aionshop_items`
-- ----------------------------
DROP TABLE IF EXISTS `aionshop_items`;
CREATE TABLE `aionshop_items` (
  `itemUniqueId` bigint(20) NOT NULL AUTO_INCREMENT,
  `itemTemplateId` bigint(20) NOT NULL,
  `itemCount` int(11) NOT NULL,
  `itemCategory` int(11) NOT NULL,
  `itemPrice` int(11) NOT NULL,
  `itemName` varchar(255) NOT NULL,
  `itemDesc` varchar(255) NOT NULL,
  `itemEyecatch` tinyint(1) NOT NULL DEFAULT '0',
  `itemClassRestrict` varchar(255) NOT NULL,
  `itemServerRestrict` varchar(255) NOT NULL,
  PRIMARY KEY (`itemUniqueId`)
) ENGINE=MyISAM AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aionshop_items
-- ----------------------------
INSERT INTO aionshop_items VALUES ('1', '187000018', '1', '7', '200', 'Storm Wing', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('2', '187000037', '1', '7', '300', 'Lucky Wing', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('3', '187000026', '1', '7', '300', 'Noble Chief Crusader\'s Wings', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('4', '166000190', '100', '8', '100', 'L190 Enchantment Stone', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('5', '168000123', '1', '8', '150', 'Godstone: Kaisinel\'s Fantasy', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('6', '168000118', '1', '8', '150', 'Godstone: Ereshkigal\'s Honor', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('7', '168000129', '1', '8', '150', 'Godstone: Beritra\'s Selfishness', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('8', '168000120', '1', '8', '150', 'Godstone: Beritra\'s Plot', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('9', '168000125', '1', '8', '150', 'Godstone: Fregion\'s Stratagem', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('10', '168000121', '1', '8', '150', 'Godstone: Fregion\'s Trick', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('11', '190020010', '1', '4', '50', 'Sassy Manduri Egg', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('12', '190020031', '1', '4', '50', 'Aqua Griffo Egg', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('13', '190020069', '1', '4', '50', 'Acute Shugo Egg', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('14', '190020089', '1', '4', '50', 'Blue Merek Egg', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('15', '190020083', '1', '4', '50', 'Blue Bulldozer Egg', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('16', '190020002', '1', '4', '50', 'Bottlenose Tion Egg', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('17', '110600996', '1', '10', '300', 'Archon Brigade General\'s Breastplate', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('18', '111600982', '1', '10', '300', 'Archon Brigade General\'s Gauntlets', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('19', '113600965', '1', '10', '300', 'Archon Brigade General\'s Greaves', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('20', '114600962', '1', '10', '300', 'Archon Brigade General\'s Sabatons', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('21', '112600955', '1', '10', '300', 'Archon Brigade General\'s Shoulderplates', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('22', '110501016', '1', '10', '300', '	\r\nArchon Brigade General\'s Hauberk', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('23', '111500991', '1', '10', '300', 'Archon Brigade General\'s Handguards', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('24', '113500994', '1', '10', '300', 'Archon Brigade General\'s Chausses', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('25', '114501003', '1', '10', '300', 'Archon Brigade General\'s Brogans', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('26', '112500940', '1', '10', '300', 'Archon Brigade General\'s Spaulders', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('27', '111100999', '1', '10', '300', 'Archon Brigade General\'s Gloves', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('28', '113101011', '1', '10', '300', 'Archon Brigade General\'s Leggings', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('29', '112100955', '1', '10', '300', 'Archon Brigade General\'s Pauldrons', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('30', '114101040', '1', '10', '300', 'Archon Brigade General\'s Shoes', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('31', '110101099', '1', '10', '300', 'Archon Brigade General\'s Tunic', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('32', '114301060', '1', '10', '300', 'Archon Brigade General\'s Boots', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('33', '113301024', '1', '10', '300', 'Archon Brigade General\'s Breeches', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('34', '110301048', '1', '10', '300', 'Archon Brigade General\'s Jerkin', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('35', '112300953', '1', '10', '300', 'Archon Brigade General\'s Shoulderguards', '', '0', '', '');
INSERT INTO aionshop_items VALUES ('36', '111301004', '1', '10', '300', 'Archon Brigade General\'s Vambrace', '', '0', '', '');
