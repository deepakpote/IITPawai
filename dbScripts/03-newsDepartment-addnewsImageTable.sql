
BEGIN;
--
-- Create model newsImage
--
CREATE TABLE `com_newsImage` (`newsImageID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `imageURL` varchar(255) NOT NULL, `createdOn` datetime NOT NULL, `createdBy` integer NOT NULL);
--
-- Remove field imageURL from news
--
ALTER TABLE `com_news` DROP COLUMN `imageURL` CASCADE;
--
-- Add field department to news
--
ALTER TABLE `com_news` ADD COLUMN `departmentCodeID` integer NULL;
ALTER TABLE `com_news` ALTER COLUMN `departmentCodeID` DROP DEFAULT;
--
-- Add field publishDate to news
--
ALTER TABLE `com_news` ADD COLUMN `publishDate` datetime DEFAULT '2017-02-01 00:00:00' NOT NULL;
ALTER TABLE `com_news` ALTER COLUMN `publishDate` DROP DEFAULT;
--
-- Add field news to newsimage
--
ALTER TABLE `com_newsImage` ADD COLUMN `newsID` integer NOT NULL;
ALTER TABLE `com_newsImage` ALTER COLUMN `newsID` DROP DEFAULT;
ALTER TABLE `com_newsImage` ADD CONSTRAINT `com_newsImage_createdBy_4e8db9a6_fk_usr_User_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `com_news_f5388581` ON `com_news` (`departmentCodeID`);
ALTER TABLE `com_news` ADD CONSTRAINT `com_news_departmentCodeID_977cf172_fk_com_code_codeID` FOREIGN KEY (`departmentCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `com_newsImage_5136587f` ON `com_newsImage` (`newsID`);
ALTER TABLE `com_newsImage` ADD CONSTRAINT `com_newsImage_newsID_2b6f139f_fk_com_news_newsID` FOREIGN KEY (`newsID`) REFERENCES `com_news` (`newsID`);

COMMIT;


