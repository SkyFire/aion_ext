ALTER TABLE `inventory`
ADD COLUMN `expireTime`  timestamp NULL DEFAULT NULL AFTER `optionalFusionSocket`;