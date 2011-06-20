-- ----------------------------
-- server_variables
-- ----------------------------

CREATE TABLE IF NOT EXISTS `server_variables` (
  `key` varchar(30) NOT NULL,
  `value` varchar(30) NOT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- players
-- ----------------------------

CREATE TABLE IF NOT EXISTS `players` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `account_id` int(11) NOT NULL,
  `account_name` varchar(50) NOT NULL,
  `exp` bigint(20) NOT NULL default '0',
  `recoverexp` bigint(20) NOT NULL default '0',
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  `heading` int(11) NOT NULL,
  `world_id` int(11) NOT NULL,
  `gender` enum('MALE','FEMALE') NOT NULL,
  `race` enum('ASMODIANS','ELYOS') NOT NULL,
  `player_class` enum('WARRIOR','GLADIATOR','TEMPLAR','SCOUT','ASSASSIN','RANGER','MAGE','SORCERER','SPIRIT_MASTER','PRIEST','CLERIC','CHANTER') NOT NULL,
  `creation_date` timestamp NULL default NULL,
  `deletion_date` timestamp NULL default NULL,
  `last_online` timestamp NULL default NULL on update CURRENT_TIMESTAMP,
  `cube_size` tinyint(1) NOT NULL default '0',
  `advanced_stigma_slot_size` TINYINT(1) NOT NULL DEFAULT '0',
  `warehouse_size` tinyint(1) NOT NULL default '0',
  `mailboxLetters` tinyint(4) NOT NULL default '0',
  `bind_point` INT NOT NULL default '0',
  `title_id` int(3) NOT NULL default '-1',
  `online` tinyint(1) NOT NULL default '0',
  `note` text,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name_unique` (`name`),
  INDEX (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- player_appearance
-- ----------------------------

CREATE TABLE IF NOT EXISTS `player_appearance` (
  `player_id` int(11) NOT NULL,
  `face` int(11) NOT NULL,
  `hair` int(11) NOT NULL,
  `deco` int(11) NOT NULL,
  `tattoo` int(11) NOT NULL,
  `skin_rgb` int(11) NOT NULL,
  `hair_rgb` int(11) NOT NULL,
  `lip_rgb` int(11) NOT NULL,
  `eye_rgb` int(11) NOT NULL,
  `face_shape` int(11) NOT NULL,
  `forehead` int(11) NOT NULL,
  `eye_height` int(11) NOT NULL,
  `eye_space` int(11) NOT NULL,
  `eye_width` int(11) NOT NULL,
  `eye_size` int(11) NOT NULL,
  `eye_shape` int(11) NOT NULL,
  `eye_angle` int(11) NOT NULL,
  `brow_height` int(11) NOT NULL,
  `brow_angle` int(11) NOT NULL,
  `brow_shape` int(11) NOT NULL,
  `nose` int(11) NOT NULL,
  `nose_bridge` int(11) NOT NULL,
  `nose_width` int(11) NOT NULL,
  `nose_tip` int(11) NOT NULL,
  `cheek` int(11) NOT NULL,
  `lip_height` int(11) NOT NULL,
  `mouth_size` int(11) NOT NULL,
  `lip_size` int(11) NOT NULL,
  `smile` int(11) NOT NULL,
  `lip_shape` int(11) NOT NULL,
  `jaw_height` int(11) NOT NULL,
  `chin_jut` int(11) NOT NULL,
  `ear_shape` int(11) NOT NULL,
  `head_size` int(11) NOT NULL,
  `neck` int(11) NOT NULL,
  `neck_length` int(11) NOT NULL,
  `shoulders` int(11) NOT NULL,
  `shoulder_size` int(11) NOT NULL,
  `torso` int(11) NOT NULL,
  `chest` int(11) NOT NULL,
  `waist` int(11) NOT NULL,
  `hips` int(11) NOT NULL,
  `arm_thickness` int(11) NOT NULL,
  `arm_length` int(11) NOT NULL,
  `hand_size` int(11) NOT NULL,
  `leg_thickness` int(11) NOT NULL,
  `leg_length` int(11) NOT NULL,
  `foot_size` int(11) NOT NULL,
  `facial_rate` int(11) NOT NULL,
  `voice` int(11) NOT NULL,
  `height` float NOT NULL,
  PRIMARY KEY (`player_id`),
  CONSTRAINT `player_id_fk` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- player_macrosses
-- ----------------------------

CREATE TABLE IF NOT EXISTS `player_macrosses` (
  `player_id` int(11) NOT NULL,
  `order` int(3) NOT NULL,
  `macro` text NOT NULL,
  UNIQUE KEY `main` (`player_id`,`order`),
  FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- player_titles
-- ----------------------------
CREATE TABLE IF NOT EXISTS `player_titles` (
  `player_id` int(11) NOT NULL,
  `title_id` int(11) NOT NULL,
  PRIMARY KEY (`player_id`,`title_id`),
  FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- friends
-- ----------------------------

CREATE TABLE IF NOT EXISTS `friends` (
  `player` int(11) NOT NULL,
  `friend` int(11) NOT NULL,
  PRIMARY KEY (`player`,`friend`),
  FOREIGN KEY (`player`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`friend`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- blocks
-- ----------------------------

CREATE TABLE IF NOT EXISTS `blocks` (
  `player` int(11) NOT NULL,
  `blocked_player` int(11) NOT NULL,
  `reason` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`player`,`blocked_player`),
  FOREIGN KEY (`player`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`blocked_player`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- player_settings
-- ----------------------------

CREATE TABLE IF NOT EXISTS `player_settings` (
  `player_id` int(11) NOT NULL,
  `settings_type` tinyint(1) NOT NULL,
  `settings` BLOB NOT NULL,
  PRIMARY KEY (`player_id`, `settings_type`),
  CONSTRAINT `ps_pl_fk` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- player_skills
-- ----------------------------

CREATE TABLE IF NOT EXISTS `player_skills` (
  `player_id` int(11) NOT NULL,
  `skillId` int(11) NOT NULL,
  `skillLevel` int(3) NOT NULL default '1',
  PRIMARY KEY  (`player_id`,`skillId`),
  FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- inventory
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inventory` (
  `itemUniqueId` int(11) NOT NULL,
  `itemId` int(11) NOT NULL,
  `itemCount` bigint(20) NOT NULL DEFAULT '0',
  `itemColor` int(11) NOT NULL DEFAULT '0',
  `itemOwner` int(11) NOT NULL,
  `isEquiped` TINYINT(1) NOT NULL DEFAULT '0',
  `isSoulBound` TINYINT(1) NOT NULL DEFAULT '0', 
  `slot` INT NOT NULL DEFAULT '0',
  `itemLocation` TINYINT(1) DEFAULT '0',
  `enchant` TINYINT(1) DEFAULT '0',
  `itemCreator` varchar(50),
  `itemSkin`  int(11) NOT NULL DEFAULT 0,
  `fusionedItem` INT(11) NOT NULL DEFAULT '0',
  `optionalSocket` INT(1) NOT NULL DEFAULT '0',
  `optionalFusionSocket` INT(1) NOT NULL DEFAULT '0',
  `expireTime` timestamp NULL DEFAULT NULL, 
  PRIMARY KEY (`itemUniqueId`),
  KEY `item_owner`(`itemOwner`),
  KEY `item_location`(`itemLocation`),
  KEY `is_equiped`(`isEquiped`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- item_stones
-- ----------------------------

CREATE TABLE IF NOT EXISTS `item_stones` (
  `itemUniqueId` int(11) NOT NULL,
  `itemId` int(11) NOT NULL,
  `slot` int(2) NOT NULL,
  `category` int(2) NOT NULL default 0,
  PRIMARY KEY (`itemUniqueId`, `slot`, `category`),
  FOREIGN KEY (`itemUniqueId`) references inventory (`itemUniqueId`) ON DELETE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- player_quests
-- ----------------------------

CREATE TABLE IF NOT EXISTS `player_quests` (
`player_id` int(11) NOT NULL,
`quest_id` int(10) unsigned NOT NULL default '0',
`status` varchar(10) NOT NULL default 'NONE',
`quest_vars` int(10) unsigned NOT NULL default '0',
`complete_count` int(3) unsigned NOT NULL default '0',
PRIMARY KEY (`player_id`,`quest_id`),
FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- droplist
-- ----------------------------

CREATE TABLE IF NOT EXISTS `droplist` (
`Id` int(11) NOT NULL AUTO_INCREMENT,
`mobId` int(11) NOT NULL DEFAULT 0,
`itemId` int(11) NOT NULL DEFAULT 0,
`min` int(11) NOT NULL DEFAULT 0,
`max` int(11) NOT NULL DEFAULT 0,
`chance` FLOAT NOT NULL DEFAULT 0,
PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- abyss_rank
-- ----------------------------

CREATE TABLE IF NOT EXISTS `abyss_rank` (
  `player_id` int(11) NOT NULL,
  `daily_ap`  int(11) NOT NULL,
  `weekly_ap` int(11) NOT NULL,
  `ap` int(11) NOT NULL,
  `rank` int(2) NOT NULL default '1',
  `top_ranking` int(5) NOT NULL DEFAULT '0',
  `old_ranking` int(5) NOT NULL DEFAULT '0',
  `daily_kill` int(5) NOT NULL,
  `weekly_kill`  int(5) NOT NULL,
  `all_kill` int(4) NOT NULL default '0',
  `max_rank` int(2) NOT NULL default '1',
  `last_kill`  int(5) NOT NULL,
  `last_ap`  int(11) NOT NULL,
  `last_update`  decimal(20,0) NOT NULL,
  PRIMARY KEY  (`player_id`),
  FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- legions
-- ----------------------------

CREATE TABLE IF NOT EXISTS `legions` (
  `id` int(11) NOT NULL,
  `name` varchar(16) NOT NULL,
  `rank` int(11) NOT NULL DEFAULT '0',
  `oldrank` int(11) NOT NULL DEFAULT '0',
  `level` int(1) NOT NULL DEFAULT '1',
  `contribution_points` INT NOT NULL DEFAULT '0',
  `legionar_permission2` int(11) NOT NULL DEFAULT '64',
  `centurion_permission1` int(11) NOT NULL DEFAULT '104',
  `centurion_permission2` int(11) NOT NULL DEFAULT '8',
  `disband_time` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name_unique` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `legion_announcement_list` (
  `legion_id` int(11) NOT NULL,
  `announcement` varchar(120) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`legion_id`) REFERENCES `legions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `legion_members` (
  `legion_id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `nickname` varchar(16) NOT NULL default '',  
  `rank` enum( 'BRIGADE_GENERAL', 'CENTURION', 'LEGIONARY' ) NOT NULL DEFAULT 'LEGIONARY',
  `selfintro` varchar(25) default '',
  PRIMARY KEY  (`player_id`),
  KEY `player_id`(`player_id`),
  FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`legion_id`) REFERENCES `legions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `legion_emblems` (
  `legion_id` int(11) NOT NULL,
  `emblem_ver` int(3) NOT NULL default '0',
  `color_r` int(3) NOT NULL default '0',  
  `color_g` int(3) NOT NULL default '0', 
  `color_b` int(3) NOT NULL default '0',
  `custom` tinyint(1) NOT NULL default '0',
	`emblem_data` longblob,
  PRIMARY KEY  (`legion_id`),
  FOREIGN KEY (`legion_id`) REFERENCES `legions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `legion_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `legion_id` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `history_type` enum('CREATE','JOIN','KICK','LEVEL_UP','APPOINTED','EMBLEM_REGISTER','EMBLEM_MODIFIED') NOT NULL,
  `name` varchar(16) NOT NULL,
  PRIMARY KEY  (`id`),
  FOREIGN KEY (`legion_id`) REFERENCES `legions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- player_recipes
-- ----------------------------
CREATE TABLE IF NOT EXISTS `player_recipes` (
  `player_id` int(11) NOT NULL,
  `recipe_id` int(11) NOT NULL,
  PRIMARY KEY  (`player_id`,`recipe_id`),
  FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- player_punisments
-- ----------------------------
CREATE TABLE IF NOT EXISTS `player_punishments` (
`player_id` int(11) NOT NULL,
`punishment_status` TINYINT UNSIGNED DEFAULT 0,
`punishment_timer` INT UNSIGNED DEFAULT 0,
PRIMARY KEY (`player_id`),
FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- mail_table
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `mail` (
`mailUniqueId` int(11) NOT NULL,
`mailRecipientId` int(11) NOT NULL,
`senderName` varchar(50) character set utf8 NOT NULL,
`mailTitle` varchar(20) character set utf8 NOT NULL,
`mailMessage` varchar(1000) character set utf8 NOT NULL,
`unread` tinyint(4) NOT NULL default '1',
`attachedItemId` int(11) NOT NULL,
`attachedKinahCount` bigint(20) NOT NULL,
`express` tinyint(4) NOT NULL default '0', 
`recievedTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
PRIMARY KEY  (`mailUniqueId`),
INDEX (`mailRecipientId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- player_effects
-- ----------------------------
CREATE TABLE IF NOT EXISTS `player_effects` (
`player_id` int(11) NOT NULL,
`skill_id` int(11) NOT NULL,
`skill_lvl` tinyint NOT NULL,
`current_time`int(11) NOT NULL,
`reuse_delay` BIGINT(13) NOT NULL,
PRIMARY KEY (`player_id`, `skill_id`),
FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- item_cooldowns
-- ----------------------------
CREATE TABLE IF NOT EXISTS `item_cooldowns` (
`player_id` int(11) NOT NULL,
`delay_id` int(11) NOT NULL,
`use_delay` SMALLINT UNSIGNED NOT NULL,
`reuse_time` BIGINT(13) NOT NULL,
PRIMARY KEY (`player_id`, `delay_id`),
FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- broker
-- ----------------------------
CREATE TABLE IF NOT EXISTS `broker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `itemPointer` int(11) NOT NULL DEFAULT '0',
  `itemId` int(11) NOT NULL,
  `itemCount` bigint(20) NOT NULL,
  `seller` varchar(16) NOT NULL,
  `price` bigint(20) NOT NULL DEFAULT '0',
  `brokerRace` enum('ELYOS','ASMODIAN') NOT NULL,
  `expireTime` timestamp NOT NULL DEFAULT '2010-01-01 02:00:00',
  `settleTime` timestamp NOT NULL DEFAULT '2010-01-01 02:00:00',
  `sellerId` int(11) NOT NULL,
  `isSold` tinyint(1) NOT NULL,
  `isSettled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- bookmark
-- ----------------------------
CREATE TABLE IF NOT EXISTS `bookmark` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) default NULL,
  `char_id` int(11) NOT NULL,
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  `world_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- `announcements`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `announcements` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `announce` text NOT NULL,
  `faction` enum('ALL','ASMODIANS','ELYOS') NOT NULL DEFAULT 'ALL',
  `type` enum('ANNOUNCE','SHOUT','ORANGE','YELLOW','NORMAL') NOT NULL DEFAULT 'ANNOUNCE',
  `delay` int(4) NOT NULL DEFAULT '1800',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


-- ----------------------------
-- `player_life_stats`
-- ----------------------------

CREATE  TABLE IF NOT EXISTS `player_life_stats` (
  `player_id` INT(11) NOT NULL ,
  `hp` INT(11) NOT NULL DEFAULT 1 ,
  `mp` INT(11) NOT NULL DEFAULT 1 ,
  `fp` INT(11) NOT NULL DEFAULT 1 ,
  PRIMARY KEY (`player_id`) )
ENGINE = MyISAM DEFAULT CHARSET=UTF8;


-- ----------------------------
-- `siege_locations`
-- ----------------------------

CREATE TABLE IF NOT EXISTS `siege_locations` (
  `id` int(11) NOT NULL,
  `race` enum('ELYOS', 'ASMODIANS', 'BALAUR') NOT NULL,
  `legion_id` int (11) NOT NULL,
  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- `petitions`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `petitions` (
  `id` bigint(11) NOT NULL,
  `playerId` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `addData` varchar(255) default NULL,
  `time` bigint(11) NOT NULL default '0',
  `status` enum('PENDING','IN_PROGRESS','REPLIED') NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `npc_shouts`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `npc_shouts` (
  `npc_id` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  `_interval` int(11) NOT NULL,
  PRIMARY KEY  (`npc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `npc_stocks`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `npc_stocks` (
`playerId` int(11) NOT NULL DEFAULT '0',
`npcId` int(11) NOT NULL DEFAULT '0',
`itemTplId` int(11) NOT NULL DEFAULT '0',
`count` int(11) NOT NULL DEFAULT '0',
`lastSaleDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
KEY `playerId` (`playerId`),
KEY `npcId` (`npcId`),
KEY `itemTplId` (`itemTplId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- `player_world_bans`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `player_world_bans` (
	`player` int(11) NOT NULL,
	`by` varchar(255) NOT NULL,
	`duration` bigint(11) NOT NULL,
	`date` bigint(11) NOT NULL,
	`reason` varchar(255) NOT NULL,
	PRIMARY KEY (`player`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- `instance_time`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `instance_time` (
  `playerId` int(11) DEFAULT NULL,
  `instanceId` int(11) DEFAULT NULL,
  `CheckIn` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- `spawns`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `spawns` (
	`spawn_id` int(11) NOT NULL AUTO_INCREMENT,
	`object_id` int(11) NOT NULL,
	`admin_id` int(11) NOT NULL,
	`group_name` varchar(255) DEFAULT NULL,
	`npc_id` int(11) NOT NULL,
	`respawn` tinyint(1) NOT NULL DEFAULT 0,
	`map_id` int(11) NOT NULL,
	`x` float(11) NOT NULL,
	`y` float(11) NOT NULL,
	`z` float(11) NOT NULL,
	`h` tinyint(8) NOT NULL,
	`spawned` tinyint(1) NOT NULL DEFAULT 0,
	`staticid` int(11) NOT NULL DEFAULT 0,
	PRIMARY KEY (`spawn_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- `spawn_groups`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `spawn_groups` (
	`admin_id` int(11) NOT NULL,
	`group_name` varchar(255) NOT NULL,
	`spawned` tinyint(1) NOT NULL DEFAULT 0,
	PRIMARY KEY (`admin_id`, `group_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- `siege_log`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `siege_log` (
  `log_uuid` bigint(20) NOT NULL auto_increment,
  `legion_name` varchar(255) NOT NULL default '',
  `action` enum('CAPTURE','DEFEND') NOT NULL,
  `tstamp` bigint(20) NOT NULL,
  `siegeloc_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`log_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- `player_pets`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `player_pets` (
`idx` int(11) NOT NULL AUTO_INCREMENT,
`player_id` int(11) NOT NULL,
`pet_id` int(11) NOT NULL,
`decoration` int(11) NOT NULL,
`name` varchar(255) NOT NULL,
PRIMARY KEY (`idx`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

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

-- ----------------------------
-- `player_passkey`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `player_passkey` (
  `account_id` int(11) NOT NULL,
  `passkey` varchar(8) NOT NULL DEFAULT '',
  PRIMARY KEY (`account_id`,`passkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `npc_shouts` VALUES (209070, 390487, 360);
INSERT INTO `npc_shouts` VALUES (799064, 390470, 360);
INSERT INTO `npc_shouts` VALUES (209028, 390487, 360);
INSERT INTO `npc_shouts` VALUES (209018, 390487, 360);
INSERT INTO `npc_shouts` VALUES (209029, 390486, 360);
INSERT INTO `npc_shouts` VALUES (209048, 390487, 360);
INSERT INTO `npc_shouts` VALUES (209038, 390487, 360);
INSERT INTO `npc_shouts` VALUES (209008, 390486, 360);
INSERT INTO `npc_shouts` VALUES (209039, 390486, 360);
INSERT INTO `npc_shouts` VALUES (203086, 390000, 360);
INSERT INTO `npc_shouts` VALUES (203121, 390293, 180);
INSERT INTO `npc_shouts` VALUES (203127, 390300, 180);
INSERT INTO `npc_shouts` VALUES (203737, 390341, 180);
INSERT INTO `npc_shouts` VALUES (204119, 390388, 180);
INSERT INTO `npc_shouts` VALUES (210077, 340272, 180);
INSERT INTO `npc_shouts` VALUES (210347, 340261, 180);
INSERT INTO `npc_shouts` VALUES (210699, 340274, 180);
INSERT INTO `npc_shouts` VALUES (730013, 390303, 180);
INSERT INTO `npc_shouts` VALUES (798390, 390464, 180);
INSERT INTO `npc_shouts` VALUES (798391, 390463, 180);
INSERT INTO `npc_shouts` VALUES (798392, 390466, 180);
INSERT INTO `npc_shouts` VALUES (798393, 390468, 180);
