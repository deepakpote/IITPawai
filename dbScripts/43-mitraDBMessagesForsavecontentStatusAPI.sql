-- Messages for save content status API.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100229 as codeID, 100 as codeGroupID, 'ContentID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save content status' as comment union
Select 100230 as codeID, 100 as codeGroupID, 'StatusCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save content status' as comment union
Select 100231 as codeID, 100 as codeGroupID, 'Content does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save content status' as comment union
Select 100232 as codeID, 100 as codeGroupID, 'Status does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save content status' as comment union
Select 100233 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save content status' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '43-mitraDBMessagesForsavecontentStatusAPI' where `key` = 'dbScriptExecutedUntil';
