BEGIN;
--
-- Create model contentGrade
--
CREATE TABLE `con_contentGrade` (`contentgradeID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY);
--
-- Remove field grade from content
--
-- Remove the index on the colum
ALTER TABLE `con_content` 
DROP FOREIGN KEY `con_content_gradeCodeID_811e36c1_fk_com_code_codeID`;
ALTER TABLE `con_content` 
DROP INDEX `con_content_gradeCodeID_811e36c1_fk_com_code_codeID` ;

ALTER TABLE `con_content` DROP COLUMN `gradeCodeID` CASCADE;

--
-- Add field content to contentgrade
--
ALTER TABLE `con_contentGrade` ADD COLUMN `contentID` integer NOT NULL;
ALTER TABLE `con_contentGrade` ALTER COLUMN `contentID` DROP DEFAULT;

--
-- Add field grade to contentgrade
--
ALTER TABLE `con_contentGrade` ADD COLUMN `gradeCodeID` integer NOT NULL;
ALTER TABLE `con_contentGrade` ALTER COLUMN `gradeCodeID` DROP DEFAULT;
CREATE INDEX `con_contentGrade_64cb7131` ON `con_contentGrade` (`contentID`);
ALTER TABLE `con_contentGrade` ADD CONSTRAINT `con_contentGrade_contentID_c92dd3ac_fk_con_Content_contentID` FOREIGN KEY (`contentID`) REFERENCES `con_content` (`contentID`);
CREATE INDEX `con_contentGrade_63b116d0` ON `con_contentGrade` (`gradeCodeID`);
ALTER TABLE `con_contentGrade` ADD CONSTRAINT `con_contentGrade_gradeCodeID_810813f1_fk_com_Code_codeID` FOREIGN KEY (`gradeCodeID`) REFERENCES `com_code` (`codeID`);

COMMIT;