-- Insert code group for Content Requirements.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 117 as codeGroupID, 'Content Requirements' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;
-- Insert into code - Content Requirements.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 117100 as codeID, 117 as codeGroupID, 'Computer' as codeNameEn, 'कॉम्पुटर' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 117101 as codeID, 117 as codeGroupID, 'Laptop' as codeNameEn, 'लॅपटॉप' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 117102 as codeID, 117 as codeGroupID, 'Tab' as codeNameEn, 'टॅब' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 117103 as codeID, 117 as codeGroupID, 'Mobile' as codeNameEn, 'मोबाईल' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 117104 as codeID, 117 as codeGroupID, 'Projector' as codeNameEn, 'प्रोजेक्टर' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 117105 as codeID, 117 as codeGroupID, 'WiFi' as codeNameEn, 'वायफाय' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- upddate existing data of requirment.
update con_content set requirement = '117100,117101,117102,117103' where requirement = 'कॉम्पुटर, लॅपटॉप, टॅब, मोबाईल';
-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '34-mitraDBCodeForContentRequirments' where `key` = 'dbScriptExecutedUntil';
