-- ----------------------------
-- `player_surveys`
-- ----------------------------
DROP TABLE IF EXISTS `player_surveys`;
CREATE TABLE `player_surveys` (
  `survey_id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `option_id` tinyint(1) NOT NULL,
  PRIMARY KEY (`survey_id`,`player_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- `surveys`
-- ----------------------------
DROP TABLE IF EXISTS `surveys`;
CREATE TABLE `surveys` (
  `survey_id` int(11) NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) NOT NULL,
  `title` varchar(127) NOT NULL,
  `message` varchar(1023) NOT NULL,
  `itemId` int(11) DEFAULT NULL,
  `itemCount` int(11) DEFAULT NULL,
  `player_level_min` tinyint(1) DEFAULT NULL,
  `player_level_max` tinyint(1) DEFAULT NULL,
  `survey_all` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`survey_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- `surveys_option`
-- ----------------------------
DROP TABLE IF EXISTS `surveys_option`;
CREATE TABLE `surveys_option` (
  `survey_id` int(11) NOT NULL AUTO_INCREMENT,
  `option_id` tinyint(1) NOT NULL,
  `option_text` varchar(255) NOT NULL,
  `itemId` int(11) DEFAULT NULL,
  `itemCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`survey_id`,`option_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
