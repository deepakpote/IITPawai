-- Messages for chapter integration in upload content.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100255 as codeID, 100 as codeGroupID, 'Chapter cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment union
Select 100256 as codeID, 100 as codeGroupID, 'Chapter does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Chapter list' as comment union
Select 100257 as codeID, 100 as codeGroupID, 'Select single grade' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment union
Select 100258 as codeID, 100 as codeGroupID, 'Instruction in English cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment union
Select 100259 as codeID, 100 as codeGroupID, 'Instruction in Marathi cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment union
Select 100260 as codeID, 100 as codeGroupID, 'Author in English cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment union
Select 100261 as codeID, 100 as codeGroupID, 'Author in Marathi cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment;


BEGIN;
--
-- Alter field contentTitle on contentdetail
--
ALTER TABLE `con_contentDetail` MODIFY `contentTitle` varchar(255) NULL;

COMMIT;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '51-mitraDBMessagesForUploadContent-ChapterIntegration' where `key` = 'dbScriptExecutedUntil';
