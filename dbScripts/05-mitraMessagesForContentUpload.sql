-- Upload content .
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100159 as codeID, 100 as codeGroupID, 'Content title cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100160 as codeID, 100 as codeGroupID, 'Content type cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100161 as codeID, 100 as codeGroupID, 'file Type cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100162 as codeID, 100 as codeGroupID, 'file Name cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100163 as codeID, 100 as codeGroupID, 'User not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100164 as codeID, 100 as codeGroupID, 'SubjectCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100165 as codeID, 100 as codeGroupID, 'TopicCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100166 as codeID, 100 as codeGroupID, 'Invalid contentType provided' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100167 as codeID, 100 as codeGroupID, 'ContentType does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100168 as codeID, 100 as codeGroupID, 'FileType does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100169 as codeID, 100 as codeGroupID, 'language does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100170 as codeID, 100 as codeGroupID, 'Content upload failed' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100171 as codeID, 100 as codeGroupID, 'GradeCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100172 as codeID, 100 as codeGroupID, 'Invalied file name' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100173 as codeID, 100 as codeGroupID, 'LanguageCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment;


select * from com_code where codeGroupID = 100;