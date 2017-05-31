-- Messages for removeNewsImage & removeNewsPDF
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100267 as codeID, 100 as codeGroupID, 'UserID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Remove news images' as comment union
Select 100268 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Remove news images' as comment union
Select 100269 as codeID, 100 as codeGroupID, 'News does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Remove news images' as comment;

Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100270 as codeID, 100 as codeGroupID, 'UserID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Remove news pdf' as comment union
Select 100271 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Remove news pdf' as comment union
Select 100272 as codeID, 100 as codeGroupID, 'News does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Remove news pdf' as comment;


-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '63-mitraDBMessagesFoRemoveNewsImageANDpdfAPI' where `key` = 'dbScriptExecutedUntil';
