-- Add code .
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100177 as codeID, 100 as codeGroupID, 'UserID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save Code' as comment union
Select 100178 as codeID, 100 as codeGroupID, 'CodeGroupID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100179 as codeID, 100 as codeGroupID, 'CodeGroupID does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100180 as codeID, 100 as codeGroupID, 'CodeName English cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100181 as codeID, 100 as codeGroupID, 'User not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100182 as codeID, 100 as codeGroupID, 'Code not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100183 as codeID, 100 as codeGroupID, 'Unable to save code details' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';