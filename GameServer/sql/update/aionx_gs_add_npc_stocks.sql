-- ----------------------------
-- Table structure for `npc_stocks`
-- ----------------------------
DROP TABLE IF EXISTS `npc_stocks`;
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
