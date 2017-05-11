-- Insert into code - department
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 112109 as codeID, 112 as codeGroupID, 'Curriculum Development Dept' as codeNameEn, 'अभ्यासक्रम िवकास विभाग ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 112110 as codeID, 112 as codeGroupID, 'Evaluation Dept' as codeNameEn, 'मूल्यमापन विभाग ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 112111 as codeID, 112 as codeGroupID, 'Research Dept' as codeNameEn, 'संशोधन विभाग ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 112112 as codeID, 112 as codeGroupID, 'Science Dept' as codeNameEn, 'िवज्ञान विभाग ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 112113 as codeID, 112 as codeGroupID, 'English Dept' as codeNameEn, 'इंग्रजी विभाग ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 112114 as codeID, 112 as codeGroupID, 'Mathematics Dept' as codeNameEn, ' गिणत विभाग ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 112115 as codeID, 112 as codeGroupID, 'Marathi Language Dept' as codeNameEn, 'मराठी भाषा विभाग ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 112116 as codeID, 112 as codeGroupID, 'Urdu Language Dept' as codeNameEn, 'उदूर् भाषा विभाग  ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 112117 as codeID, 112 as codeGroupID, 'Other Languages Dept' as codeNameEn, 'इतर भाषा विभाग ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 112118 as codeID, 112 as codeGroupID, 'Pre Service Education, Child Psychology' as codeNameEn, 'प्री सिव्हर् स एजुकेशन, बाल मानसशास्त्र ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 112119 as codeID, 112 as codeGroupID, 'Other Languages Dept' as codeNameEn, 'इतर भाषा विभाग ' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- Update the old departments name.
Update com_code set codeNameEn = 'IT and Publications Department', codeNameMr = 'आयटी आिण प्रकाशन िवभाग ' where codeID = 112103;
Update com_code set codeNameEn = 'Social Studies, Arts and Sports' where codeID = 112105;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '61-mitraDBAddNewsDepartment' where `key` = 'dbScriptExecutedUntil';
