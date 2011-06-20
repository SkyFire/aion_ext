-- ----------------------------
-- renames players advenced_stigma_slot_size to advanced_stigma_slot_size.
-- ----------------------------
ALTER TABLE `players` CHANGE COLUMN `advenced_stigma_slot_size` `advanced_stigma_slot_size` TINYINT(1) NOT NULL DEFAULT '0';
