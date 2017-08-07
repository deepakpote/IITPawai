-- Insert code group for Training Category.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 122 as codeGroupID, 'Training Category' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- Insert into code - Training Category type
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 122100 as codeID, 122 as codeGroupID, 'State' as codeNameEn, 'महाराष्ट्र' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn; 

-- Add messages for block list.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100285 as codeID, 100 as codeGroupID, 'DistrictCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'block list' as comment union
Select 100286 as codeID, 100 as codeGroupID, 'DistrictCodeID not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'block list' as comment union
Select 100287 as codeID, 100 as codeGroupID, 'No records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'block list' as comment;


BEGIN;
--
-- Add field state to eventdetail
--
ALTER TABLE `evt_eventDetail` ADD COLUMN `stateCodeID` integer DEFAULT 121100 NOT NULL;
ALTER TABLE `evt_eventDetail` ALTER COLUMN `stateCodeID` DROP DEFAULT;
CREATE INDEX `evt_eventDetail_5796ca69` ON `evt_eventDetail` (`stateCodeID`);
ALTER TABLE `evt_eventDetail` ADD CONSTRAINT `evt_eventDetail_stateCodeID_840fcc4f_fk_com_code_codeID` FOREIGN KEY (`stateCodeID`) REFERENCES `com_code` (`codeID`);

COMMIT;

BEGIN;
--
-- Create model usersEvent
--
CREATE TABLE `evt_usersEvent` (`userEventID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `createdOn` datetime NOT NULL, `eventDetailID` integer NOT NULL, `userID` integer NOT NULL);
ALTER TABLE `evt_usersEvent` ADD CONSTRAINT `evt_user_eventDetailID_8b6a0ea7_fk_evt_eventDetail_eventDetailID` FOREIGN KEY (`eventDetailID`) REFERENCES `evt_eventDetail` (`eventDetailID`);
ALTER TABLE `evt_usersEvent` ADD CONSTRAINT `evt_usersEvent_userID_61fc42fa_fk_usr_User_userID` FOREIGN KEY (`userID`) REFERENCES `usr_User` (`userID`);

COMMIT;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '75-mitraDBForTrainingCategory' where `key` = 'dbScriptExecutedUntil';

