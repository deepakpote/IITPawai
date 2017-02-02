BEGIN;
--
-- Alter field codeNameEn on code
--
ALTER TABLE `com_code` MODIFY `codeNameEn` varchar(255) NOT NULL;

COMMIT;