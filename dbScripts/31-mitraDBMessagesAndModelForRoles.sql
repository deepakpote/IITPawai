BEGIN;
--
-- Create model role
--
CREATE TABLE `usr_role` (`roleID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `roleName` varchar(255) NOT NULL, `createdOn` datetime NOT NULL);
--
-- Create model userRole
--
CREATE TABLE `usr_userRole` (`userRoleID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `createdOn` datetime NOT NULL, `roleID` integer NOT NULL, `userID` integer NOT NULL);
--
-- Alter unique_together for userrole (1 constraint(s))
--
ALTER TABLE `usr_userRole` ADD CONSTRAINT `usr_userRole_userID_ff1847f6_uniq` UNIQUE (`userID`, `roleID`);
ALTER TABLE `usr_userRole` ADD CONSTRAINT `usr_userRole_roleID_24d1c91b_fk_usr_role_roleID` FOREIGN KEY (`roleID`) REFERENCES `usr_role` (`roleID`);
ALTER TABLE `usr_userRole` ADD CONSTRAINT `usr_userRole_userID_b0047122_fk_usr_user_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);


-- Add messages for user role list .
Insert into com_code(codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100203 as codeID, 100 as codeGroupID, 'UserID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'user role list' as comment union
Select 100204 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'user role list' as comment union
Select 100205 as codeID, 100 as codeGroupID, 'No records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'user role list ' as comment;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '31-mitraDBMessagesAndModelForRoles' where `key` = 'dbScriptExecutedUntil';

COMMIT;