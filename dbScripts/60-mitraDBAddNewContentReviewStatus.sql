-- Add new content review status "Archive".
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 114103 as codeID, 114 as codeGroupID, 'Archive' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add new content review status' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '60-mitraDBAddNewContentReviewStatus' where `key` = 'dbScriptExecutedUntil';

