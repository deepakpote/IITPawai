# update codeid for 112101 as 112102 and then update 112100 as 112101
update 	com_code set 	codeNameEn = 'General' where codeID = 112100;

update 	com_code set 	codeNameEn = 'Deparment 1' where codeID = 112101;

insert into com_code(codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
select 112102 as codeID, 112 as codeGroupID, 'Deparment 2' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn
;
