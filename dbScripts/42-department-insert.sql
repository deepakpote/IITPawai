-- Update department 1 and 2 to actual departments
Update com_code set codeNameEn = 'Human resources Dept', codeNameMr = 'मानव संसाधन  विभाग ' where codeID = 112101;
Update com_code set codeNameEn = 'Co-ordination Dept', codeNameMr = 'समन्वय विभाग ' where codeID = 112102;

-- Insert into code - department
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 112103 as codeID, 112 as codeGroupID, 'IT Dept' as codeNameEn, 'आय टी व प्रसार माध्यम कक्ष ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 112104 as codeID, 112 as codeGroupID, 'Language Dept' as codeNameEn, 'भाषा विभाग ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 112105 as codeID, 112 as codeGroupID, 'Social Science Dept' as codeNameEn, 'सामाजिक शास्र, कला व क्रीडा विभाग ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 112106 as codeID, 112 as codeGroupID, 'Continuous Professional Development Dept' as codeNameEn, 'सेवापूर्व शिक्षण, बालशिक्षण मानसशास्र' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 112107 as codeID, 112 as codeGroupID, 'Equity Cell' as codeNameEn, 'समता कक्ष ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 112108 as codeID, 112 as codeGroupID, 'International Cell' as codeNameEn, 'आंतरराष्ट्रीय गुणवत्ता कक्ष ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn
;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '42-department-insert' where `key` = 'dbScriptExecutedUntil';
