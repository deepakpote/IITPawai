-- Add new user types
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 109104 as codeID, 109 as codeGroupID, 'HM' as codeNameEn, ' प्राचार्य' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add new user type' as comment union
Select 109105 as codeID, 109 as codeGroupID, 'Parent' as codeNameEn, 'पालक' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add new user type' as comment;


update com_code set codeNameEn = 'Kendra Pramukh' where codeID = 109103;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '67-mitraDBAddUserTypes' where `key` = 'dbScriptExecutedUntil';

