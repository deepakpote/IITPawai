-- save news .
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100209 as codeID, 100 as codeGroupID, 'Content title English cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100210 as codeID, 100 as codeGroupID, 'Content title Marathi cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100211 as codeID, 100 as codeGroupID, 'News category cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100212 as codeID, 100 as codeGroupID, 'News department cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100213 as codeID, 100 as codeGroupID, 'News importance cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100214 as codeID, 100 as codeGroupID, 'News status cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save News' as comment union
Select 100215 as codeID, 100 as codeGroupID, 'User not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100216 as codeID, 100 as codeGroupID, 'News status does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100217 as codeID, 100 as codeGroupID, 'News category does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100218 as codeID, 100 as codeGroupID, 'News department does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100219 as codeID, 100 as codeGroupID, 'News importance does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100220 as codeID, 100 as codeGroupID, 'News save failed' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment;


-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '39-mitraDBMessagesForsaveNews' where `key` = 'dbScriptExecutedUntil';
