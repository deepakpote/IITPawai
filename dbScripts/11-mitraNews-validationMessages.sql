INSERT into com_code (codeID, codeGroupID, codeNameEn, `comment`,  createdBy, createdOn, modifiedBy, ModifiedOn)

select 100175 as codeID,  100 as codeGroupID, ' Department does not exists' as codeNameEn, 'Get news list' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100176 as codeID,  100 as codeGroupID, ' Publish from date should not be greater than publish to date' as codeNameEn, 'Get news list' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn