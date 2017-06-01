BEGIN;
--
-- Alter field phoneNumber on user
--
ALTER TABLE `usr_user` DROP INDEX `phoneNumber`;

COMMIT;

-- Messages for update user profile
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100273 as codeID, 100 as codeGroupID, 'Update user profile validation failed.' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Update user profile' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '65-mitraDBAlterUserTableRemoveUnique' where `key` = 'dbScriptExecutedUntil';



