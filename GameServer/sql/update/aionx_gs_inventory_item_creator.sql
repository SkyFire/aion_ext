-- ----------------------------
-- adds the Item Creator column to the Inventory table.
-- ----------------------------
ALTER TABLE `inventory` ADD `itemCreator` VARCHAR(50) AFTER `enchant` ;
