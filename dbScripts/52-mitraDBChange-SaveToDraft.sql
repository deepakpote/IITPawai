-- content upload save to draft change

BEGIN;
--
-- Alter field fileName on content
--
ALTER TABLE `con_content` MODIFY `fileName` varchar(255) NULL;
--
-- Alter field fileType on content
--
ALTER TABLE `con_content` DROP FOREIGN KEY `con_content_fileTypeCodeID_06b22d94_fk_com_code_codeID`;
ALTER TABLE `con_content` MODIFY `fileTypeCodeID` integer NULL;
ALTER TABLE `con_content` ADD CONSTRAINT `con_content_fileTypeCodeID_9bd0e164_fk_com_code_codeID` FOREIGN KEY (`fileTypeCodeID`) REFERENCES `com_code` (`codeID`);
--
-- Alter field language on content
--
ALTER TABLE `con_content` DROP FOREIGN KEY `con_content_languageCodeID_14351205_fk_com_code_codeID`;
ALTER TABLE `con_content` MODIFY `languageCodeID` integer NULL;
ALTER TABLE `con_content` ADD CONSTRAINT `con_content_languageCodeID_c19674fd_fk_com_code_codeID` FOREIGN KEY (`languageCodeID`) REFERENCES `com_code` (`codeID`);

-- update into com_configuration
update com_configuration set `value` = '52-mitraDBChange-SaveToDraft' where `key` = 'dbScriptExecutedUntil';

COMMIT;