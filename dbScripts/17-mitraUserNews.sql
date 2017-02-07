
BEGIN;
--
-- Create model userNews
--
CREATE TABLE `com_userNews` (`userNewsID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `createdOn` datetime NOT NULL, `newsID` integer NOT NULL, `userID` integer NOT NULL);
--

--
-- Alter unique_together for usernews (1 constraint(s))
--
ALTER TABLE `com_userNews` ADD CONSTRAINT `com_userNews_newsID_a3e20d7a_uniq` UNIQUE (`newsID`, `userID`);
ALTER TABLE `com_userNews` ADD CONSTRAINT `com_userNews_newsID_1a3397af_fk_com_news_newsID` FOREIGN KEY (`newsID`) REFERENCES `com_news` (`newsID`);
ALTER TABLE `com_userNews` ADD CONSTRAINT `com_userNews_userID_188829da_fk_usr_User_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);

COMMIT;