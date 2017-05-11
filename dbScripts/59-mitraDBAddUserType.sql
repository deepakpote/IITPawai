-- Add user type 1] Officer 2] Teacher 3] Other.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 109101 as codeID, 109 as codeGroupID, 'Officer' as codeNameEn, 'अधिकारी' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add new user type' as comment union
Select 109102 as codeID, 109 as codeGroupID, 'Other' as codeNameEn, 'इतर' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add new user Type' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '59-mitraDBAddUserType' where `key` = 'dbScriptExecutedUntil';
