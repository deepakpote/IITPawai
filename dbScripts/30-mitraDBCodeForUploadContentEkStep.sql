-- Insert into code - FileTypeCodeID - Ek step
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 108105 as codeID, 108 as codeGroupID, 'Ek Step' as codeNameEn, 'एक स्टेप' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn; 

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '30-mitraDBCodeForUploadContentEkStep' where `key` = 'dbScriptExecutedUntil';
