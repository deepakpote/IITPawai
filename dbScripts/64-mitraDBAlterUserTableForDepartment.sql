BEGIN;
--
-- Add field department to user
--
ALTER TABLE `usr_user` ADD COLUMN `departmentCodeID` integer NULL;
ALTER TABLE `usr_user` ALTER COLUMN `departmentCodeID` DROP DEFAULT;
--
--
CREATE INDEX `usr_user_f5388581` ON `usr_user` (`departmentCodeID`);
ALTER TABLE `usr_user` ADD CONSTRAINT `usr_user_departmentCodeID_e68396d5_fk_com_code_codeID` FOREIGN KEY (`departmentCodeID`) REFERENCES `com_code` (`codeID`);

COMMIT;

Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 109103 as codeID, 109 as codeGroupID, 'Center Head' as codeNameEn, 'केंद्रप्रमुख' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add new user type' as comment;

-- update into com_configuration
update com_configuration set `value` = '64-mitraDBAlterUserTableForDepartment' where `key` = 'dbScriptExecutedUntil';
