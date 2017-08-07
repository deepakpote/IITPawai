-- Add messages for save training status.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100276 as codeID, 100 as codeGroupID, 'EventDetailID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save training Status' as comment union
Select 100277 as codeID, 100 as codeGroupID, 'Status cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save training Status' as comment union
Select 100278 as codeID, 100 as codeGroupID, 'Event does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save training Status' as comment union
Select 100279 as codeID, 100 as codeGroupID, 'Status does not exist' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save training Status' as comment union
Select 100280 as codeID, 100 as codeGroupID, 'User not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save training Status' as comment;


-- Insert code group for Calendar.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 121 as codeGroupID, 'State' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- Insert into code - Training Status
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 121100 as codeID, 121 as codeGroupID, 'Maharashtra' as codeNameEn, 'महाराष्ट्र' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;


BEGIN;
--
-- Add field state to districtblockmapping
--
ALTER TABLE `evt_districtBlockMapping` ADD COLUMN `stateCodeID` integer NULL;
ALTER TABLE `evt_districtBlockMapping` ALTER COLUMN `stateCodeID` DROP DEFAULT;
--
-- Alter unique_together for districtblockmapping (1 constraint(s))
--
ALTER TABLE `evt_districtBlockMapping` ADD CONSTRAINT `evt_districtBlockMapping_districtCodeID_5b5d2add_uniq` UNIQUE (`districtCodeID`, `blockCodeID`, `stateCodeID`);
CREATE INDEX `evt_districtBlockMapping_5796ca69` ON `evt_districtBlockMapping` (`stateCodeID`);
ALTER TABLE `evt_districtBlockMapping` ADD CONSTRAINT `evt_districtBlockMapping_stateCodeID_143b1eab_fk_com_code_codeID` FOREIGN KEY (`stateCodeID`) REFERENCES `com_code` (`codeID`);

COMMIT;

update evt_districtBlockMapping set stateCodeID = 121100;


BEGIN;
--
-- Alter field state on districtblockmapping
--
ALTER TABLE `evt_districtBlockMapping` DROP FOREIGN KEY `evt_districtBlockMapping_stateCodeID_143b1eab_fk_com_code_codeID`;
ALTER TABLE `evt_districtBlockMapping` ALTER COLUMN `stateCodeID` SET DEFAULT 121100;
UPDATE `evt_districtBlockMapping` SET `stateCodeID` = 121100 WHERE `stateCodeID` IS NULL;
ALTER TABLE `evt_districtBlockMapping` MODIFY `stateCodeID` integer NOT NULL;
ALTER TABLE `evt_districtBlockMapping` ADD CONSTRAINT `evt_districtBlockMapping_stateCodeID_143b1eab_fk_com_code_codeID` FOREIGN KEY (`stateCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `evt_districtBlockMapping` ALTER COLUMN `stateCodeID` DROP DEFAULT;

COMMIT;

-- Add messages for save training status.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100281 as codeID, 100 as codeGroupID, 'No training records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Search trainings' as comment union
Select 100282 as codeID, 100 as codeGroupID, 'No alternate training records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Search alternate trainings' as comment;

-- Add messages for save training status.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100283 as codeID, 100 as codeGroupID, 'EventID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Search trainings' as comment union
Select 100284 as codeID, 100 as codeGroupID, 'Event not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Search alternate trainings' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '74-mitraDBmsgForSaveTrainingStatus' where `key` = 'dbScriptExecutedUntil';

