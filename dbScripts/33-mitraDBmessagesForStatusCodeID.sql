-- Add messages for search teaching AID & searchSelfLearning API.
Insert into com_code(codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100208 as codeID, 100 as codeGroupID, 'Status does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Search teaching aid/selff learning' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '33-mitraDBmessagesForStatusCodeID' where `key` = 'dbScriptExecutedUntil';
