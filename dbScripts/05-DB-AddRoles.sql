

INSERT INTO `com_configuration`(`configurationID`,`key`,`value`) VALUES (1,'comCodeVersion',1);


INSERT INTO `usr_role`(`roleID`,`roleName`,`createdOn`) VALUES(1,'Admin',now());
INSERT INTO `usr_role`(`roleID`,`roleName`,`createdOn`) VALUES(2,'Teacher',now());


INSERT INTO `com_configuration`(`configurationID`,`key`,`value`) VALUES (3,'minAppVersion',1);

INSERT INTO `usr_userrole`(`createdOn`,`roleID`,`userID`) VALUES (now(),1,1);

-- common message.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100311 as codeID, 100 as codeGroupID, 'User doesnot exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'common' as comment;


delete from com_code where codeGroupID = 104;

INSERT INTO `com_code` VALUES (104101,'Middle','मध्य',NULL,NULL,now(),now(),104,1,1),
(104102,'Secondary ','माध्यमिक',NULL,NULL,now(),now(),104,1,1),
(104103,'Sr Secondary','वरिष्ठ माध्यमिक',NULL,NULL,now(),now(),104,1,1);

-- Add chapters
/* Grade secondary (9-10) - Subject  Algebra */

INSERT INTO con_chapter(createdOn,modifiedOn,createdBy,gradeCodeID,modifiedBy,subjectCodeID) VALUES (now(),now(),1,104102,1,103100);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('Algebraic Expressions','बीजगणित अभिव्यक्ती',LAST_INSERT_ID());

INSERT INTO con_chapter(createdOn,modifiedOn,createdBy,gradeCodeID,modifiedBy,subjectCodeID) VALUES (now(),now(),1,104102,1,103100);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('Arithmetic Progression','अंकगणित प्रगती',LAST_INSERT_ID());


/* Grade secondary (9-10) - Subject  Geometry*/


INSERT INTO con_chapter(createdOn,modifiedOn,createdBy,gradeCodeID,modifiedBy,subjectCodeID) VALUES (now(),now(),1,104102,1,103101);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('Coordinate Geometry','समन्वय भूमिती',LAST_INSERT_ID());

INSERT INTO con_chapter(createdOn,modifiedOn,createdBy,gradeCodeID,modifiedBy,subjectCodeID) VALUES (now(),now(),1,104102,1,103101);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('Aala Paoos Aala','मंडळ',LAST_INSERT_ID());