-- Insert code group for FCM Notification type.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 123 as codeGroupID, 'FCM Notification type' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- Insert into code - FCM Notification type
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 123100 as codeID, 123 as codeGroupID, 'Teaching aids' as codeNameEn, ' शैक्षणिक साधने' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 123101 as codeID, 123 as codeGroupID, 'Self learning' as codeNameEn, ' स्वअध्ययन' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 123102 as codeID, 123 as codeGroupID, 'News' as codeNameEn, 'बातम्या' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 123103 as codeID, 123 as codeGroupID, 'Trainings' as codeNameEn, ' प्रशिक्षणे' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 123104 as codeID, 123 as codeGroupID, 'Other' as codeNameEn, 'इतर' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn; 

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '70-mitraDBAddFCMNotificationType' where `key` = 'dbScriptExecutedUntil';