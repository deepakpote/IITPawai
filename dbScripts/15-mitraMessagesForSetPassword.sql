-- Add messages for set password API .
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100184 as codeID, 100 as codeGroupID, 'User not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'set password' as comment union
Select 100185 as codeID, 100 as codeGroupID, 'Password should not contain any space' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'set password' as comment union
Select 100186 as codeID, 100 as codeGroupID, 'Password cannot be empty.It should be gretter then 6 character and less then 255 character' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'set password' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';