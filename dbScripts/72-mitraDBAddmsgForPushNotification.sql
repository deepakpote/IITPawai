-- Add messages for get content details for specific all language.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100296 as codeID, 100 as codeGroupID, 'Send notification user not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Send push notifications' as comment union
Select 100297 as codeID, 100 as codeGroupID, 'Notification type not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Send push notifications' as comment;



BEGIN;
--
-- Create model fcmNotificationResponse
--
CREATE TABLE `usr_fcmNotificationResponse` (`fcmResponseID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `fcmDevicesIDs` longtext NULL, `objectID` integer NULL, `title` longtext NULL, `body` longtext NULL, `responseMessage` longtext NULL, `createdOn` datetime NOT NULL, `createdBy` integer NULL, `notificationTypeCodeID` integer NOT NULL);
ALTER TABLE `usr_fcmNotificationResponse` ADD CONSTRAINT `usr_fcmNotificationRespons_createdBy_f90ad872_fk_usr_user_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `usr_fcmNotificationResponse` ADD CONSTRAINT `usr_fcmNotifi_notificationTypeCodeID_3009f946_fk_com_code_codeID` FOREIGN KEY (`notificationTypeCodeID`) REFERENCES `com_code` (`codeID`);

COMMIT;
-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '72-mitraDBAddmsgForPushNotification' where `key` = 'dbScriptExecutedUntil';
