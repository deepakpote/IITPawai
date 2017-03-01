BEGIN;
--
-- Remove field author from content
--
ALTER TABLE `con_content` DROP COLUMN `author` CASCADE;
--
-- Remove field contentTitle from content
--
ALTER TABLE `con_content` DROP COLUMN `contentTitle` CASCADE;
--
-- Remove field instruction from content
--
ALTER TABLE `con_content` DROP COLUMN `instruction` CASCADE;


--
-- Add field status to content
--
ALTER TABLE `con_content` ADD COLUMN `statusCodeID` integer DEFAULT 114100 NOT NULL;
ALTER TABLE `con_content` ALTER COLUMN `statusCodeID` DROP DEFAULT;
CREATE INDEX `con_content_df3158f0` ON `con_content` (`statusCodeID`);
ALTER TABLE `con_content` ADD CONSTRAINT `con_content_statusCodeID_e54247c8_fk_com_code_codeID` FOREIGN KEY (`statusCodeID`) REFERENCES `com_code` (`codeID`);

-- update into com_configuration
update com_configuration set `value` = '27-mitra-db-update-model-content' where `key` = 'dbScriptExecutedUntil';


COMMIT;