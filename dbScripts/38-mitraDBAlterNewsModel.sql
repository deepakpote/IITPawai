BEGIN;
--
-- Remove field author from news
--
ALTER TABLE `com_news` DROP COLUMN `author` CASCADE;
--
-- Remove field content from news
--
ALTER TABLE `com_news` DROP COLUMN `content` CASCADE;
--
-- Remove field newsTitle from news
--
ALTER TABLE `com_news` DROP COLUMN `newsTitle` CASCADE;
--
-- Add field newsCategory to news
--
ALTER TABLE `com_news` ADD COLUMN `newsCategoryCodeID` integer DEFAULT 115100 NOT NULL;
ALTER TABLE `com_news` ALTER COLUMN `newsCategoryCodeID` DROP DEFAULT;
--
-- Add field newsImportance to news
--
ALTER TABLE `com_news` ADD COLUMN `newsImportanceCodeID` integer DEFAULT 116101 NOT NULL;
ALTER TABLE `com_news` ALTER COLUMN `newsImportanceCodeID` DROP DEFAULT;
--
-- Add field status to news
--
ALTER TABLE `com_news` ADD COLUMN `statusCodeID` integer DEFAULT 114100 NOT NULL;
ALTER TABLE `com_news` ALTER COLUMN `statusCodeID` DROP DEFAULT;
--
-- Alter unique_together for newsimage (1 constraint(s))
--
ALTER TABLE `com_newsImage` ADD CONSTRAINT `com_newsImage_newsID_c0247d3c_uniq` UNIQUE (`newsID`, `imageURL`);
CREATE INDEX `com_news_223fe48f` ON `com_news` (`newsCategoryCodeID`);
ALTER TABLE `com_news` ADD CONSTRAINT `com_news_newsCategoryCodeID_8f31ddf4_fk_com_code_codeID` FOREIGN KEY (`newsCategoryCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `com_news_063d89e6` ON `com_news` (`newsImportanceCodeID`);
ALTER TABLE `com_news` ADD CONSTRAINT `com_news_newsImportanceCodeID_b05d03ba_fk_com_code_codeID` FOREIGN KEY (`newsImportanceCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `com_news_df3158f0` ON `com_news` (`statusCodeID`);
ALTER TABLE `com_news` ADD CONSTRAINT `com_news_statusCodeID_269da012_fk_com_code_codeID` FOREIGN KEY (`statusCodeID`) REFERENCES `com_code` (`codeID`);


-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '38-mitraDBAlterNewsModel' where `key` = 'dbScriptExecutedUntil';


COMMIT;