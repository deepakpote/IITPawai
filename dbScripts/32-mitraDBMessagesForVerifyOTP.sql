-- Add messages for Search teaching Aid,self learning and contentList .
Insert into com_code(codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100206 as codeID, 100 as codeGroupID, 'fcmRegistrationRequired cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'VerifyOTP ' as comment union
Select 100207 as codeID, 100 as codeGroupID, 'fcmRegistrationRequired value must be boolean' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'VerifyOTP ' as comment;

update com_code set codeNameEn = 'Password cannot be empty.It should be greater then 6 characters and less then 16 characters' 
where codeID = 100186;


BEGIN;
--
-- Add field password to token
--
ALTER TABLE `usr_token` ADD COLUMN `password` varchar(255) NULL;
ALTER TABLE `usr_token` ALTER COLUMN `password` DROP DEFAULT;

COMMIT;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '32-mitraDBMessagesForVerifyOTP' where `key` = 'dbScriptExecutedUntil';