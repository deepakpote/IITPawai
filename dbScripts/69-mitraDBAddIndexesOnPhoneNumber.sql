-- Add indexes to user and device table on phonenumber

BEGIN;
--
-- Alter field phoneNumber on device
--
CREATE INDEX `usr_device_phoneNumber_db487d2e_uniq` ON `usr_device` (`phoneNumber`);
--
-- Alter field phoneNumber on user
--
CREATE INDEX `usr_user_phoneNumber_0e4a7e99_uniq` ON `usr_user` (`phoneNumber`);

COMMIT;


-- update into com_configuration
update com_configuration set `value` = '69-mitraDBAddIndexesOnPhoneNumber' where `key` = 'dbScriptExecutedUntil';