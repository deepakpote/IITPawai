-- Messages for chapter integration in upload content. Conflict resolved
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100262 as codeID, 100 as codeGroupID, 'Either upload file or give filename' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment union
Select 100263 as codeID, 100 as codeGroupID, 'Upload valid file' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'upload content' as comment;


-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '53-mitraDBMessagesForUploadContent' where `key` = 'dbScriptExecutedUntil';