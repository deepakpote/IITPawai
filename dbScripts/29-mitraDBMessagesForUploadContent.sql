-- Add messages for Search teaching Aid,self learning and contentList .
Insert into com_code(codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100201 as codeID, 100 as codeGroupID, 'Content status cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'content upload ' as comment union
Select 100200 as codeID, 100 as codeGroupID, 'Content status not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'content upload ' as comment;

BEGIN;
--
-- Alter field author on contentdetail
--
ALTER TABLE `con_contentDetail` MODIFY `author` varchar(255) NULL;

COMMIT;



-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '29-mitraDBMessagesForUploadContent' where `key` = 'dbScriptExecutedUntil';
