BEGIN;
--
-- Add field pdfFileURL to news
--
ALTER TABLE `com_news` ADD COLUMN `pdfFileURL` varchar(255) NULL;
ALTER TABLE `com_news` ALTER COLUMN `pdfFileURL` DROP DEFAULT;

COMMIT;