-- select * from com_code where codeGroupID = 100;
-- Add messages for training.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100298 as codeID, 100 as codeGroupID, 'event does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100299 as codeID, 100 as codeGroupID, 'event date cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100300 as codeID, 100 as codeGroupID, 'stateCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100301 as codeID, 100 as codeGroupID, 'districtCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100302 as codeID, 100 as codeGroupID, 'blockCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100303 as codeID, 100 as codeGroupID, 'trainer name in Marathi cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100304 as codeID, 100 as codeGroupID, 'trainer name in English cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100305 as codeID, 100 as codeGroupID, 'statusCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100306 as codeID, 100 as codeGroupID, 'user does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100307 as codeID, 100 as codeGroupID, 'event title in marathi cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100308 as codeID, 100 as codeGroupID, 'eventDetailID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100309 as codeID, 100 as codeGroupID, 'eventDetailID does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100310 as codeID, 100 as codeGroupID, 'userID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment;


-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '76-mitraDBMsgForTraing' where `key` = 'dbScriptExecutedUntil';