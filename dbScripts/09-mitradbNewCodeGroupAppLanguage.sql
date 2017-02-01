-- Insert code group for App Language.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 113 as codeGroupID, 'App Language' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn
;

-- Insert into code - App Language.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 113100 as codeID, 113 as codeGroupID, 'English' as codeNameEn, 'इंग्रजी' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 113101 as codeID, 113 as codeGroupID, 'Marathi' as codeNameEn, ' मराठी' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- update com_codeGroup for Content Language
update com_codeGroup set codeGroupName = 'Content Language' where codeGroupID = 101;

-- update com code version
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';