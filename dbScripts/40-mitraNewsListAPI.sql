-- NewsList & userNewsList.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100221 as codeID, 100 as codeGroupID, 'News category does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'News list' as comment union
Select 100222 as codeID, 100 as codeGroupID, 'News status does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'News list' as comment union
Select 100223 as codeID, 100 as codeGroupID, 'No records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'News list' as comment union
Select 100224 as codeID, 100 as codeGroupID, 'AppLanguageCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'News list' as comment union
Select 100225 as codeID, 100 as codeGroupID, 'AppLanguageCodeID does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'News list' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '40-mitraNewsListAPI' where `key` = 'dbScriptExecutedUntil';
