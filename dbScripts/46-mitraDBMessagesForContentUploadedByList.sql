-- Messages for content uploaded by users list API.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100239 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Content uploaded by list' as comment union
Select 100240 as codeID, 100 as codeGroupID, 'No records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Content uploaded by list' as comment;

-- Insert code group for content uploaded by self search.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 118 as codeGroupID, 'Content / News uploaded by user' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- Insert into code - for content / News uploaded by self search.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 118100 as codeID, 118 as codeGroupID, 'Me' as codeNameEn, 'मी' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn; 



-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '46-mitraDBMessagesForContentUploadedByList' where `key` = 'dbScriptExecutedUntil';
