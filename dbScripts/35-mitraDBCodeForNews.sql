-- Insert code group for News.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 115 as codeGroupID, 'News Category' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 116 as codeGroupID, 'News Importance' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;


update com_codeGroup set codeGroupName = 'Content / News / Training Creation Status' where codeGroupID = 114;

-- Insert into code - News.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 115100 as codeID, 115 as codeGroupID, 'M.A.A' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 116100 as codeID, 116 as codeGroupID, 'High Priority' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 116101 as codeID, 116 as codeGroupID, 'No Priroty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 116102 as codeID, 116 as codeGroupID, 'Low Prioity' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;


-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '35-mitraDBCodeForNews' where `key` = 'dbScriptExecutedUntil';