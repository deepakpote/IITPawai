BEGIN;
--
-- Create model contentDetail
--
CREATE TABLE `con_contentDetail` (`contentDetailID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `contentTitle` varchar(255) NOT NULL, `instruction` longtext NULL, `author` varchar(255) NOT NULL, `appLanguageCodeID` integer NOT NULL, `contentID` integer NOT NULL);
--
-- Alter unique_together for contentdetail (1 constraint(s))
--
ALTER TABLE `con_contentDetail` ADD CONSTRAINT `con_contentDetail_contentID_e0d722fb_uniq` UNIQUE (`contentID`, `appLanguageCodeID`);
ALTER TABLE `con_contentDetail` ADD CONSTRAINT `con_contentDetail_appLanguageCodeID_40ea9f2e_fk_com_code_codeID` FOREIGN KEY (`appLanguageCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `con_contentDetail` ADD CONSTRAINT `con_contentDetail_contentID_29e854cd_fk_con_content_contentID` FOREIGN KEY (`contentID`) REFERENCES `con_content` (`contentID`);

-- update into com_configuration
update com_configuration set `value` = '24-mitra-db-model-contentDetail' where `key` = 'dbScriptExecutedUntil';


COMMIT;