BEGIN;
--
-- Create model newsDetail
--
CREATE TABLE `com_newsDetail` (`newsDetailID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `newsTitle` varchar(255) NOT NULL, `author` varchar(255) NULL, `content` longtext NULL, `tags` longtext NULL, `appLanguageCodeID` integer NOT NULL, `newsID` integer NOT NULL);
--
-- Alter unique_together for newsdetail (1 constraint(s))
--
ALTER TABLE `com_newsDetail` ADD CONSTRAINT `com_newsDetail_newsID_d710f1f7_uniq` UNIQUE (`newsID`, `appLanguageCodeID`);
ALTER TABLE `com_newsDetail` ADD CONSTRAINT `com_newsDetail_appLanguageCodeID_b1873f2b_fk_com_code_codeID` FOREIGN KEY (`appLanguageCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `com_newsDetail` ADD CONSTRAINT `com_newsDetail_newsID_baf678a5_fk_com_news_newsID` FOREIGN KEY (`newsID`) REFERENCES `com_news` (`newsID`);

COMMIT;

-- update com code version.
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';

-- update into com_configuration
update com_configuration set `value` = '36-mitraDBNewsDetailModel' where `key` = 'dbScriptExecutedUntil';