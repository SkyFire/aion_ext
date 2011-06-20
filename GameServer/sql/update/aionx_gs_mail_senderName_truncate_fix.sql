-- ----------------------------
-- changes the mail senderName to be longer so the truncation error will not occur.
-- ----------------------------
ALTER TABLE `mail` CHANGE COLUMN `senderName` `senderName` varchar(50) character set utf8 NOT NULL;
