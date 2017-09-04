-- Common app models DB script ..
BEGIN;
--
-- Create model code
--
CREATE TABLE `com_code` (`codeID` integer NOT NULL PRIMARY KEY, `codeNameEn` varchar(255) NOT NULL, `codeNameMr` varchar(255) NULL, `displayOrder` integer NULL, `comment` varchar(255) NULL, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL);
--
-- Create model codeGroup
--
CREATE TABLE `com_codeGroup` (`codeGroupID` integer NOT NULL PRIMARY KEY, `codeGroupName` varchar(255) NOT NULL UNIQUE, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL);
--
-- Create model configuration
--
CREATE TABLE `com_configuration` (`configurationID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `key` varchar(255) NOT NULL, `value` varchar(1000) NOT NULL);
--
-- Create model news
--
CREATE TABLE `com_news` (`newsID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `publishDate` datetime NOT NULL, `pdfFileURL` varchar(255) NULL, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL);
--
-- Create model newsDetail
--
CREATE TABLE `com_newsDetail` (`newsDetailID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `newsTitle` varchar(255) NOT NULL, `author` varchar(255) NULL, `content` longtext NULL, `tags` longtext NULL);
--
-- Create model newsImage
--
CREATE TABLE `com_newsImage` (`newsImageID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `imageURL` varchar(255) NOT NULL, `createdOn` datetime NOT NULL);
--
-- Create model userNews
--
CREATE TABLE `com_userNews` (`userNewsID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `createdOn` datetime NOT NULL, `newsID` integer NOT NULL);
ALTER TABLE `com_userNews` ADD CONSTRAINT `com_userNews_newsID_1a3397af_fk_com_news_newsID` FOREIGN KEY (`newsID`) REFERENCES `com_news` (`newsID`);

COMMIT;