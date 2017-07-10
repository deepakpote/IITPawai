-- Add messages for get content details for specific all language.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100294 as codeID, 100 as codeGroupID, 'App language cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , ' get content details for specific all language' as comment union
Select 100295 as codeID, 100 as codeGroupID, 'App language not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , ' get content details for specific all language' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '71-mitraDBAddmsgForContentDetails' where `key` = 'dbScriptExecutedUntil';

