-- Add messages for get content details for specific all language.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100288 as codeID, 100 as codeGroupID, 'Invalid token' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Send data notification to all' as comment union
Select 100289 as codeID, 100 as codeGroupID, 'NotificationTypeCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Send data notification to all' as comment union
Select 100290 as codeID, 100 as codeGroupID, 'ObjectID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Send data notification to all' as comment union
Select 100291 as codeID, 100 as codeGroupID, 'Marathi title cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Send data notification to all' as comment union
Select 100292 as codeID, 100 as codeGroupID, 'Marathi text cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Send data notification to all' as comment union
Select 100293 as codeID, 100 as codeGroupID, 'No FCM deviceID found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Send data notification to all' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '71-mitraDBAddmsgForContentDetails' where `key` = 'dbScriptExecutedUntil';