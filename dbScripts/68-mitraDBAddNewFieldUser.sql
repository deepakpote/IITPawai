-- Add new field - user 
BEGIN;
--
-- Add field user to device
--
ALTER TABLE `usr_device` ADD COLUMN `userID` integer NULL;
ALTER TABLE `usr_device` ALTER COLUMN `userID` DROP DEFAULT;
CREATE INDEX `usr_device_585c9570` ON `usr_device` (`userID`);
ALTER TABLE `usr_device` ADD CONSTRAINT `usr_device_userID_931b6ede_fk_usr_user_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);

COMMIT;

BEGIN;
--
-- Alter field phoneNumber on device
--
ALTER TABLE `usr_device` MODIFY `phoneNumber` varchar(15) NULL;

COMMIT;

BEGIN;
--
-- Alter unique_together for device (1 constraint(s))
--
ALTER TABLE `usr_device` DROP INDEX `usr_device_phoneNumber_059a0ebc_uniq`;
ALTER TABLE `usr_device` ADD CONSTRAINT `usr_device_userID_357afeb8_uniq` UNIQUE (`userID`, `fcmDeviceID`);

COMMIT;

-- update into com_configuration
update com_configuration set `value` = '68-mitraDBAddNewFieldUser' where `key` = 'dbScriptExecutedUntil';


