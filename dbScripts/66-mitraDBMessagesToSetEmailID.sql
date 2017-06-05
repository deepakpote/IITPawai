-- Messages for set New EmailID
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100275 as codeID, 100 as codeGroupID, 'User with this email already exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Set emailID' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '66-mitraDBMessagesToSetEmailID' where `key` = 'dbScriptExecutedUntil';
