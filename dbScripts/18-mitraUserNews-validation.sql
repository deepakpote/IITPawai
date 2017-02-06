
INSERT into com_code (codeID, codeGroupID, codeNameEn, `comment`,  createdBy, createdOn, modifiedBy, ModifiedOn)

select 100191 as codeID,  100 as codeGroupID, 'User does not exists' as codeNameEn, 'Save user news' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100192 as codeID,  100 as codeGroupID, 'News does not exists' as codeNameEn, 'Save user news' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100193 as codeID,  100 as codeGroupID, 'UserID cannot be empty' as codeNameEn, 'Save user news' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100194 as codeID,  100 as codeGroupID, 'NewsID cannot be empty' as codeNameEn, 'Save user news' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100195 as codeID,  100 as codeGroupID, 'Department does not exists' as codeNameEn, 'Get user''s news list' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100196 as codeID,  100 as codeGroupID, 'Publish from date should not be greater than publish to date' as codeNameEn, 'Get user''s news list' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union 
select 100197 as codeID,  100 as codeGroupID, 'You have already saved this news' as codeNameEn, 'Save user news ' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union 
select 100198 as codeID,  100 as codeGroupID, 'User does not exists' as codeNameEn, 'Get user''s news list' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100199 as codeID,  100 as codeGroupID, 'UserID cannot be empty' as codeNameEn, 'Get user''s news list' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn


delete from com_code where codeID in (100195, 100196);

update com_code set codeID = 100195 where codeID  = 100198;
update com_code set codeID = 100196 where codeID  = 100199;