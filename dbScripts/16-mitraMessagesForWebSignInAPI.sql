-- Add messages for webSignIn API .
Insert into com_code(codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100187 as codeID, 100 as codeGroupID, 'PhoneNumber cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'webSignIn' as comment union
Select 100188 as codeID, 100 as codeGroupID, 'Password cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'webSignIn' as comment union
Select 100189 as codeID, 100 as codeGroupID, 'Invalid phoneNumber' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'webSignIn' as comment union
Select 100190 as codeID, 100 as codeGroupID, 'Invalid login credentials' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'webSignIn' as comment;


-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

