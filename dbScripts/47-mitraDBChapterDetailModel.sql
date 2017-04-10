BEGIN;
--
-- Create model chapter
--
CREATE TABLE `con_chapter` (`chapterID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `displayOrder` integer NULL, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL, `createdBy` integer NOT NULL, `gradeCodeID` integer NOT NULL, `modifiedBy` integer NOT NULL, `subjectCodeID` integer NOT NULL);
--
-- Create model chapterDetail
--
CREATE TABLE `con_chapterDetail` (`chapterDetailID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `chapterEng` longtext NOT NULL, `chapterMar` longtext NOT NULL, `chapterID` integer NOT NULL);
ALTER TABLE `con_chapter` ADD CONSTRAINT `con_chapter_createdBy_4fac67d0_fk_usr_user_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `con_chapter` ADD CONSTRAINT `con_chapter_gradeCodeID_410f18c7_fk_com_code_codeID` FOREIGN KEY (`gradeCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `con_chapter` ADD CONSTRAINT `con_chapter_modifiedBy_0c5710f4_fk_usr_user_userID` FOREIGN KEY (`modifiedBy`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `con_chapter` ADD CONSTRAINT `con_chapter_subjectCodeID_74e7544f_fk_com_code_codeID` FOREIGN KEY (`subjectCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `con_chapterDetail` ADD CONSTRAINT `con_chapterDetail_chapterID_1d0ca4ad_fk_con_chapter_chapterID` FOREIGN KEY (`chapterID`) REFERENCES `con_chapter` (`chapterID`);

COMMIT;

BEGIN;
--
-- Add field chapter to content
--
ALTER TABLE `con_content` ADD COLUMN `chapterID` integer NULL;
ALTER TABLE `con_content` ALTER COLUMN `chapterID` DROP DEFAULT;
CREATE INDEX `con_content_3837e609` ON `con_content` (`chapterID`);
ALTER TABLE `con_content` ADD CONSTRAINT `con_content_chapterID_8e68c597_fk_con_chapter_chapterID` FOREIGN KEY (`chapterID`) REFERENCES `con_chapter` (`chapterID`);

COMMIT;


-- update into com_configuration
update com_configuration set `value` = '47-mitraDBChapterDetailModel' where `key` = 'dbScriptExecutedUntil';

