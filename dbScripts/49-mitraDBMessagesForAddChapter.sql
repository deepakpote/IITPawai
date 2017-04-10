-- Messages for Add Chapter API.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100241 as codeID, 100 as codeGroupID, 'Subject cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union
Select 100242 as codeID, 100 as codeGroupID, 'Grade cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union
Select 100243 as codeID, 100 as codeGroupID, 'Chapter name in English cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union
Select 100244 as codeID, 100 as codeGroupID, 'Chapter name in Marathi cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union
Select 100245 as codeID, 100 as codeGroupID, 'Subject does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union
Select 100246 as codeID, 100 as codeGroupID, 'Grade does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union
Select 100247 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union 
Select 100254 as codeID, 100 as codeGroupID, 'Chapter does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '49-mitraDBMessagesForAddChapter' where `key` = 'dbScriptExecutedUntil';
