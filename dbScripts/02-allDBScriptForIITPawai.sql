


-- common 0002


BEGIN;
--
-- Add field user to usernews
--
ALTER TABLE `com_userNews` ADD COLUMN `userID` integer NOT NULL;
ALTER TABLE `com_userNews` ALTER COLUMN `userID` DROP DEFAULT;
--
-- Add field createdBy to newsimage
--
ALTER TABLE `com_newsImage` ADD COLUMN `createdBy` integer NOT NULL;
ALTER TABLE `com_newsImage` ALTER COLUMN `createdBy` DROP DEFAULT;
--
-- Add field news to newsimage
--
ALTER TABLE `com_newsImage` ADD COLUMN `newsID` integer NOT NULL;
ALTER TABLE `com_newsImage` ALTER COLUMN `newsID` DROP DEFAULT;
--
-- Add field appLanguage to newsdetail
--
ALTER TABLE `com_newsDetail` ADD COLUMN `appLanguageCodeID` integer NOT NULL;
ALTER TABLE `com_newsDetail` ALTER COLUMN `appLanguageCodeID` DROP DEFAULT;
--
-- Add field news to newsdetail
--
ALTER TABLE `com_newsDetail` ADD COLUMN `newsID` integer NOT NULL;
ALTER TABLE `com_newsDetail` ALTER COLUMN `newsID` DROP DEFAULT;
--
-- Add field createdBy to news
--
ALTER TABLE `com_news` ADD COLUMN `createdBy` integer NOT NULL;
ALTER TABLE `com_news` ALTER COLUMN `createdBy` DROP DEFAULT;
--
-- Add field department to news
--
ALTER TABLE `com_news` ADD COLUMN `departmentCodeID` integer NULL;
ALTER TABLE `com_news` ALTER COLUMN `departmentCodeID` DROP DEFAULT;
--
-- Add field modifiedBy to news
--
ALTER TABLE `com_news` ADD COLUMN `modifiedBy` integer NOT NULL;
ALTER TABLE `com_news` ALTER COLUMN `modifiedBy` DROP DEFAULT;
--
-- Add field newsCategory to news
--
ALTER TABLE `com_news` ADD COLUMN `newsCategoryCodeID` integer NOT NULL;
ALTER TABLE `com_news` ALTER COLUMN `newsCategoryCodeID` DROP DEFAULT;
--
-- Add field newsImportance to news
--
ALTER TABLE `com_news` ADD COLUMN `newsImportanceCodeID` integer NOT NULL;
ALTER TABLE `com_news` ALTER COLUMN `newsImportanceCodeID` DROP DEFAULT;
--
-- Add field status to news
--
ALTER TABLE `com_news` ADD COLUMN `statusCodeID` integer NOT NULL;
ALTER TABLE `com_news` ALTER COLUMN `statusCodeID` DROP DEFAULT;
--
-- Add field createdBy to codegroup
--
ALTER TABLE `com_codeGroup` ADD COLUMN `createdBy` integer NOT NULL;
ALTER TABLE `com_codeGroup` ALTER COLUMN `createdBy` DROP DEFAULT;
--
-- Add field modifiedBy to codegroup
--
ALTER TABLE `com_codeGroup` ADD COLUMN `modifiedBy` integer NOT NULL;
ALTER TABLE `com_codeGroup` ALTER COLUMN `modifiedBy` DROP DEFAULT;
--
-- Add field codeGroup to code
--
ALTER TABLE `com_code` ADD COLUMN `codeGroupID` integer NOT NULL;
ALTER TABLE `com_code` ALTER COLUMN `codeGroupID` DROP DEFAULT;
--
-- Add field createdBy to code
--
ALTER TABLE `com_code` ADD COLUMN `createdBy` integer NOT NULL;
ALTER TABLE `com_code` ALTER COLUMN `createdBy` DROP DEFAULT;
--
-- Add field modifiedBy to code
--
ALTER TABLE `com_code` ADD COLUMN `modifiedBy` integer NOT NULL;
ALTER TABLE `com_code` ALTER COLUMN `modifiedBy` DROP DEFAULT;
--
-- Alter unique_together for usernews (1 constraint(s))
--
ALTER TABLE `com_userNews` ADD CONSTRAINT `com_userNews_newsID_a3e20d7a_uniq` UNIQUE (`newsID`, `userID`);
--
-- Alter unique_together for newsimage (1 constraint(s))
--
ALTER TABLE `com_newsImage` ADD CONSTRAINT `com_newsImage_newsID_c0247d3c_uniq` UNIQUE (`newsID`, `imageURL`);
--
-- Alter unique_together for newsdetail (1 constraint(s))
--


COMMIT;


-- users 0001

BEGIN;
--
-- Create model user
--
CREATE TABLE `usr_user` (`userID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `phoneNumber` varchar(15) NULL, `userName` varchar(100) NOT NULL, `photoUrl` varchar(255) NULL, `udiseCode` varchar(255) NULL, `emailID` varchar(100) NULL UNIQUE, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL, `createdBy` integer NULL, `departmentCodeID` integer NULL, `districtCodeID` integer NULL, `modifiedBy` integer NULL, `preferredLanguageCodeID` integer NULL, `userTypeCodeID` integer NULL);
--
-- Create model device
--
CREATE TABLE `usr_device` (`deviceID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `phoneNumber` varchar(15) NULL, `fcmDeviceID` varchar(255) NOT NULL, `createdOn` datetime NOT NULL, `userID` integer NULL);
--
-- Create model fcmNotificationResponse
--
CREATE TABLE `usr_fcmNotificationResponse` (`fcmResponseID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `fcmDevicesIDs` longtext NULL, `objectID` integer NULL, `title` longtext NULL, `body` longtext NULL, `responseMessage` longtext NULL, `createdOn` datetime NOT NULL, `createdBy` integer NULL, `notificationTypeCodeID` integer NOT NULL);
--
-- Create model otp
--
CREATE TABLE `usr_otp` (`otpID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `phoneNumber` varchar(15) NOT NULL, `otp` varchar(6) NOT NULL, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL);
--
-- Create model role
--
CREATE TABLE `usr_role` (`roleID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `roleName` varchar(255) NOT NULL, `createdOn` datetime NOT NULL);
--
-- Create model token
--
CREATE TABLE `usr_token` (`tokenID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `token` varchar(255) NOT NULL UNIQUE, `password` varchar(255) NULL, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL, `userID` integer NOT NULL);
--
-- Create model userAuth
--
CREATE TABLE `usr_userAuth` (`userAuthID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `loginID` varchar(255) NOT NULL UNIQUE, `password` varchar(255) NULL, `authToken` varchar(255) NULL, `lastLoggedInOn` datetime NOT NULL, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL, `createdBy` integer NOT NULL, `modifiedBy` integer NOT NULL);
--
-- Create model userContent
--
CREATE TABLE `usr_userContent` (`userContentID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `createdOn` datetime NOT NULL, `contentID` integer NOT NULL, `userID` integer NOT NULL);
--
-- Create model userGrade
--
CREATE TABLE `usr_userGrade` (`userGradeID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL, `gradeCodeID` integer NOT NULL, `userID` integer NOT NULL);
--
-- Create model userRole
--
CREATE TABLE `usr_userRole` (`userRoleID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `createdOn` datetime NOT NULL, `roleID` integer NOT NULL, `userID` integer NOT NULL);
--
-- Create model userSkill
--
CREATE TABLE `usr_userSkill` (`userSkillID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL, `skillCodeID` integer NOT NULL, `userID` integer NOT NULL);
--
-- Create model userSubject
--
CREATE TABLE `usr_userSubject` (`userSubjectID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL, `subjectCodeID` integer NOT NULL, `userID` integer NOT NULL);
--
-- Create model userTopic
--
CREATE TABLE `usr_userTopic` (`userTopicID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL, `topicCodeID` integer NOT NULL, `userID` integer NOT NULL);
--
-- Alter unique_together for userrole (1 constraint(s))
--
ALTER TABLE `usr_userRole` ADD CONSTRAINT `usr_userRole_userID_ff1847f6_uniq` UNIQUE (`userID`, `roleID`);
--
-- Alter unique_together for usercontent (1 constraint(s))
--
ALTER TABLE `usr_userContent` ADD CONSTRAINT `usr_userContent_contentID_87d0967c_uniq` UNIQUE (`contentID`, `userID`);
--
-- Alter unique_together for device (1 constraint(s))
--
ALTER TABLE `usr_device` ADD CONSTRAINT `usr_device_userID_357afeb8_uniq` UNIQUE (`userID`, `fcmDeviceID`);
ALTER TABLE `usr_user` ADD CONSTRAINT `usr_user_createdBy_88641f72_fk_usr_user_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `usr_user` ADD CONSTRAINT `usr_user_departmentCodeID_e68396d5_fk_com_code_codeID` FOREIGN KEY (`departmentCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `usr_user` ADD CONSTRAINT `usr_user_districtCodeID_53391cbc_fk_com_code_codeID` FOREIGN KEY (`districtCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `usr_user` ADD CONSTRAINT `usr_user_modifiedBy_c41541a2_fk_usr_user_userID` FOREIGN KEY (`modifiedBy`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `usr_user` ADD CONSTRAINT `usr_user_preferredLanguageCodeID_22f7724c_fk_com_code_codeID` FOREIGN KEY (`preferredLanguageCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `usr_user` ADD CONSTRAINT `usr_user_userTypeCodeID_717fed63_fk_com_code_codeID` FOREIGN KEY (`userTypeCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `usr_user_c9aa10b2` ON `usr_user` (`phoneNumber`);
ALTER TABLE `usr_device` ADD CONSTRAINT `usr_device_userID_931b6ede_fk_usr_user_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `usr_device_c9aa10b2` ON `usr_device` (`phoneNumber`);
ALTER TABLE `usr_fcmNotificationResponse` ADD CONSTRAINT `usr_fcmNotificationRespons_createdBy_f90ad872_fk_usr_user_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `usr_fcmNotificationResponse` ADD CONSTRAINT `usr_fcmNotifi_notificationTypeCodeID_3009f946_fk_com_code_codeID` FOREIGN KEY (`notificationTypeCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `usr_token` ADD CONSTRAINT `usr_token_userID_6a26feec_fk_usr_user_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `usr_userAuth` ADD CONSTRAINT `usr_userAuth_createdBy_a4640497_fk_usr_user_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `usr_userAuth` ADD CONSTRAINT `usr_userAuth_modifiedBy_1ddb2901_fk_usr_user_userID` FOREIGN KEY (`modifiedBy`) REFERENCES `usr_user` (`userID`);

ALTER TABLE `usr_userContent` ADD CONSTRAINT `usr_userContent_userID_85f72762_fk_usr_user_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `usr_userGrade` ADD CONSTRAINT `usr_userGrade_gradeCodeID_0fc437d5_fk_com_code_codeID` FOREIGN KEY (`gradeCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `usr_userGrade` ADD CONSTRAINT `usr_userGrade_userID_0f6fb289_fk_usr_user_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `usr_userRole` ADD CONSTRAINT `usr_userRole_roleID_24d1c91b_fk_usr_role_roleID` FOREIGN KEY (`roleID`) REFERENCES `usr_role` (`roleID`);
ALTER TABLE `usr_userRole` ADD CONSTRAINT `usr_userRole_userID_b0047122_fk_usr_user_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `usr_userSkill` ADD CONSTRAINT `usr_userSkill_skillCodeID_3e69cd87_fk_com_code_codeID` FOREIGN KEY (`skillCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `usr_userSkill` ADD CONSTRAINT `usr_userSkill_userID_ed2c2242_fk_usr_user_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `usr_userSubject` ADD CONSTRAINT `usr_userSubject_subjectCodeID_eb6b350c_fk_com_code_codeID` FOREIGN KEY (`subjectCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `usr_userSubject` ADD CONSTRAINT `usr_userSubject_userID_5ea1cdce_fk_usr_user_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `usr_userTopic` ADD CONSTRAINT `usr_userTopic_topicCodeID_d0fd12ba_fk_com_code_codeID` FOREIGN KEY (`topicCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `usr_userTopic` ADD CONSTRAINT `usr_userTopic_userID_138e1b69_fk_usr_user_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);




ALTER TABLE `com_newsDetail` ADD CONSTRAINT `com_newsDetail_newsID_d710f1f7_uniq` UNIQUE (`newsID`, `appLanguageCodeID`);
CREATE INDEX `com_userNews_585c9570` ON `com_userNews` (`userID`);
ALTER TABLE `com_userNews` ADD CONSTRAINT `com_userNews_userID_188829da_fk_usr_user_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `com_newsImage_34998857` ON `com_newsImage` (`createdBy`);
ALTER TABLE `com_newsImage` ADD CONSTRAINT `com_newsImage_createdBy_4e8db9a6_fk_usr_user_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `com_newsImage_5136587f` ON `com_newsImage` (`newsID`);
ALTER TABLE `com_newsImage` ADD CONSTRAINT `com_newsImage_newsID_2b6f139f_fk_com_news_newsID` FOREIGN KEY (`newsID`) REFERENCES `com_news` (`newsID`);
CREATE INDEX `com_newsDetail_602ce6f6` ON `com_newsDetail` (`appLanguageCodeID`);
ALTER TABLE `com_newsDetail` ADD CONSTRAINT `com_newsDetail_appLanguageCodeID_b1873f2b_fk_com_code_codeID` FOREIGN KEY (`appLanguageCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `com_newsDetail_5136587f` ON `com_newsDetail` (`newsID`);
ALTER TABLE `com_newsDetail` ADD CONSTRAINT `com_newsDetail_newsID_baf678a5_fk_com_news_newsID` FOREIGN KEY (`newsID`) REFERENCES `com_news` (`newsID`);
CREATE INDEX `com_news_34998857` ON `com_news` (`createdBy`);
ALTER TABLE `com_news` ADD CONSTRAINT `com_news_createdBy_a3d29178_fk_usr_user_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `com_news_f5388581` ON `com_news` (`departmentCodeID`);
ALTER TABLE `com_news` ADD CONSTRAINT `com_news_departmentCodeID_977cf172_fk_com_code_codeID` FOREIGN KEY (`departmentCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `com_news_91d33cc8` ON `com_news` (`modifiedBy`);
ALTER TABLE `com_news` ADD CONSTRAINT `com_news_modifiedBy_b9c2e64e_fk_usr_user_userID` FOREIGN KEY (`modifiedBy`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `com_news_223fe48f` ON `com_news` (`newsCategoryCodeID`);
ALTER TABLE `com_news` ADD CONSTRAINT `com_news_newsCategoryCodeID_8f31ddf4_fk_com_code_codeID` FOREIGN KEY (`newsCategoryCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `com_news_063d89e6` ON `com_news` (`newsImportanceCodeID`);
ALTER TABLE `com_news` ADD CONSTRAINT `com_news_newsImportanceCodeID_b05d03ba_fk_com_code_codeID` FOREIGN KEY (`newsImportanceCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `com_news_df3158f0` ON `com_news` (`statusCodeID`);
ALTER TABLE `com_news` ADD CONSTRAINT `com_news_statusCodeID_269da012_fk_com_code_codeID` FOREIGN KEY (`statusCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `com_codeGroup_34998857` ON `com_codeGroup` (`createdBy`);
ALTER TABLE `com_codeGroup` ADD CONSTRAINT `com_codeGroup_createdBy_8ada4bb0_fk_usr_user_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `com_codeGroup_91d33cc8` ON `com_codeGroup` (`modifiedBy`);
ALTER TABLE `com_codeGroup` ADD CONSTRAINT `com_codeGroup_modifiedBy_5f0d68a1_fk_usr_user_userID` FOREIGN KEY (`modifiedBy`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `com_code_ecf73129` ON `com_code` (`codeGroupID`);
ALTER TABLE `com_code` ADD CONSTRAINT `com_code_codeGroupID_588b043d_fk_com_codeGroup_codeGroupID` FOREIGN KEY (`codeGroupID`) REFERENCES `com_codeGroup` (`codeGroupID`);
CREATE INDEX `com_code_34998857` ON `com_code` (`createdBy`);
ALTER TABLE `com_code` ADD CONSTRAINT `com_code_createdBy_088668d4_fk_usr_user_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `com_code_91d33cc8` ON `com_code` (`modifiedBy`);
ALTER TABLE `com_code` ADD CONSTRAINT `com_code_modifiedBy_b07205c4_fk_usr_user_userID` FOREIGN KEY (`modifiedBy`) REFERENCES `usr_user` (`userID`);









COMMIT;

-- xontents 0001

BEGIN;
--
-- Create model chapter
--
CREATE TABLE `con_chapter` (`chapterID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `displayOrder` integer NULL, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL);
--
-- Create model chapterDetail
--
CREATE TABLE `con_chapterDetail` (`chapterDetailID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `chapterEng` longtext NOT NULL, `chapterMar` longtext NOT NULL);
--
-- Create model content
--
CREATE TABLE `con_content` (`contentID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `requirement` longtext NULL, `fileName` varchar(255) NULL, `objectives` longtext NULL, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL);
--
-- Create model contentDetail
--
CREATE TABLE `con_contentDetail` (`contentDetailID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `contentTitle` varchar(255) NULL, `instruction` longtext NULL, `author` varchar(255) NULL);
--
-- Create model contentGrade
--
CREATE TABLE `con_contentGrade` (`contentGradeID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY);
--
-- Create model contentResponse
--
CREATE TABLE `con_contentResponse` (`contentResponseID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `hasLiked` bool NULL, `downloadCount` integer NULL, `sharedCount` integer NULL, `contentID` integer NOT NULL);
ALTER TABLE `con_contentResponse` ADD CONSTRAINT `con_contentResponse_contentID_5102027a_fk_con_content_contentID` FOREIGN KEY (`contentID`) REFERENCES `con_content` (`contentID`);

COMMIT;


-- content 0002

BEGIN;
--
-- Add field user to contentresponse
--
ALTER TABLE `con_contentResponse` ADD COLUMN `userID` integer NOT NULL;
ALTER TABLE `con_contentResponse` ALTER COLUMN `userID` DROP DEFAULT;
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
--
-- Add field appLanguage to contentdetail
--
ALTER TABLE `con_contentDetail` ADD COLUMN `appLanguageCodeID` integer NOT NULL;
ALTER TABLE `con_contentDetail` ALTER COLUMN `appLanguageCodeID` DROP DEFAULT;
--
-- Add field content to contentdetail
--
ALTER TABLE `con_contentDetail` ADD COLUMN `contentID` integer NOT NULL;
ALTER TABLE `con_contentDetail` ALTER COLUMN `contentID` DROP DEFAULT;
--
-- Add field chapter to content
--
ALTER TABLE `con_content` ADD COLUMN `chapterID` integer NULL;
ALTER TABLE `con_content` ALTER COLUMN `chapterID` DROP DEFAULT;
--
-- Add field contentType to content
--
ALTER TABLE `con_content` ADD COLUMN `contentTypeCodeID` integer NOT NULL;
ALTER TABLE `con_content` ALTER COLUMN `contentTypeCodeID` DROP DEFAULT;
--
-- Add field createdBy to content
--
ALTER TABLE `con_content` ADD COLUMN `createdBy` integer NOT NULL;
ALTER TABLE `con_content` ALTER COLUMN `createdBy` DROP DEFAULT;
--
-- Add field fileType to content
--
ALTER TABLE `con_content` ADD COLUMN `fileTypeCodeID` integer NULL;
ALTER TABLE `con_content` ALTER COLUMN `fileTypeCodeID` DROP DEFAULT;
--
-- Add field language to content
--
ALTER TABLE `con_content` ADD COLUMN `languageCodeID` integer NULL;
ALTER TABLE `con_content` ALTER COLUMN `languageCodeID` DROP DEFAULT;
--
-- Add field modifiedBy to content
--
ALTER TABLE `con_content` ADD COLUMN `modifiedBy` integer NOT NULL;
ALTER TABLE `con_content` ALTER COLUMN `modifiedBy` DROP DEFAULT;
--
-- Add field status to content
--
ALTER TABLE `con_content` ADD COLUMN `statusCodeID` integer NOT NULL;
ALTER TABLE `con_content` ALTER COLUMN `statusCodeID` DROP DEFAULT;
--
-- Add field subject to content
--
ALTER TABLE `con_content` ADD COLUMN `subjectCodeID` integer NULL;
ALTER TABLE `con_content` ALTER COLUMN `subjectCodeID` DROP DEFAULT;
--
-- Add field topic to content
--
ALTER TABLE `con_content` ADD COLUMN `topicCodeID` integer NULL;
ALTER TABLE `con_content` ALTER COLUMN `topicCodeID` DROP DEFAULT;
--
-- Add field chapter to chapterdetail
--
ALTER TABLE `con_chapterDetail` ADD COLUMN `chapterID` integer NOT NULL;
ALTER TABLE `con_chapterDetail` ALTER COLUMN `chapterID` DROP DEFAULT;
--
-- Add field createdBy to chapter
--
ALTER TABLE `con_chapter` ADD COLUMN `createdBy` integer NOT NULL;
ALTER TABLE `con_chapter` ALTER COLUMN `createdBy` DROP DEFAULT;
--
-- Add field grade to chapter
--
ALTER TABLE `con_chapter` ADD COLUMN `gradeCodeID` integer NOT NULL;
ALTER TABLE `con_chapter` ALTER COLUMN `gradeCodeID` DROP DEFAULT;
--
-- Add field modifiedBy to chapter
--
ALTER TABLE `con_chapter` ADD COLUMN `modifiedBy` integer NOT NULL;
ALTER TABLE `con_chapter` ALTER COLUMN `modifiedBy` DROP DEFAULT;
--
-- Add field subject to chapter
--
ALTER TABLE `con_chapter` ADD COLUMN `subjectCodeID` integer NOT NULL;
ALTER TABLE `con_chapter` ALTER COLUMN `subjectCodeID` DROP DEFAULT;
--
-- Alter unique_together for contentdetail (1 constraint(s))
--
ALTER TABLE `con_contentDetail` ADD CONSTRAINT `con_contentDetail_contentID_e0d722fb_uniq` UNIQUE (`contentID`, `appLanguageCodeID`);
CREATE INDEX `con_contentResponse_585c9570` ON `con_contentResponse` (`userID`);
ALTER TABLE `con_contentResponse` ADD CONSTRAINT `con_contentResponse_userID_97f624f2_fk_usr_user_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `con_contentGrade_64cb7131` ON `con_contentGrade` (`contentID`);
ALTER TABLE `con_contentGrade` ADD CONSTRAINT `con_contentGrade_contentID_c92dd3ac_fk_con_content_contentID` FOREIGN KEY (`contentID`) REFERENCES `con_content` (`contentID`);
CREATE INDEX `con_contentGrade_63b116d0` ON `con_contentGrade` (`gradeCodeID`);
ALTER TABLE `con_contentGrade` ADD CONSTRAINT `con_contentGrade_gradeCodeID_810813f1_fk_com_code_codeID` FOREIGN KEY (`gradeCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `con_contentDetail_602ce6f6` ON `con_contentDetail` (`appLanguageCodeID`);
ALTER TABLE `con_contentDetail` ADD CONSTRAINT `con_contentDetail_appLanguageCodeID_40ea9f2e_fk_com_code_codeID` FOREIGN KEY (`appLanguageCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `con_contentDetail_64cb7131` ON `con_contentDetail` (`contentID`);
ALTER TABLE `con_contentDetail` ADD CONSTRAINT `con_contentDetail_contentID_29e854cd_fk_con_content_contentID` FOREIGN KEY (`contentID`) REFERENCES `con_content` (`contentID`);
CREATE INDEX `con_content_3837e609` ON `con_content` (`chapterID`);
ALTER TABLE `con_content` ADD CONSTRAINT `con_content_chapterID_8e68c597_fk_con_chapter_chapterID` FOREIGN KEY (`chapterID`) REFERENCES `con_chapter` (`chapterID`);
CREATE INDEX `con_content_9d675e94` ON `con_content` (`contentTypeCodeID`);
ALTER TABLE `con_content` ADD CONSTRAINT `con_content_contentTypeCodeID_dfd71c89_fk_com_code_codeID` FOREIGN KEY (`contentTypeCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `con_content_34998857` ON `con_content` (`createdBy`);
ALTER TABLE `con_content` ADD CONSTRAINT `con_content_createdBy_739fb5a3_fk_usr_user_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `con_content_d2d4aa9d` ON `con_content` (`fileTypeCodeID`);
ALTER TABLE `con_content` ADD CONSTRAINT `con_content_fileTypeCodeID_9bd0e164_fk_com_code_codeID` FOREIGN KEY (`fileTypeCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `con_content_e6a1d30a` ON `con_content` (`languageCodeID`);
ALTER TABLE `con_content` ADD CONSTRAINT `con_content_languageCodeID_c19674fd_fk_com_code_codeID` FOREIGN KEY (`languageCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `con_content_91d33cc8` ON `con_content` (`modifiedBy`);
ALTER TABLE `con_content` ADD CONSTRAINT `con_content_modifiedBy_d1200f98_fk_usr_user_userID` FOREIGN KEY (`modifiedBy`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `con_content_df3158f0` ON `con_content` (`statusCodeID`);
ALTER TABLE `con_content` ADD CONSTRAINT `con_content_statusCodeID_e54247c8_fk_com_code_codeID` FOREIGN KEY (`statusCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `con_content_4141fbda` ON `con_content` (`subjectCodeID`);
ALTER TABLE `con_content` ADD CONSTRAINT `con_content_subjectCodeID_0464a12c_fk_com_code_codeID` FOREIGN KEY (`subjectCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `con_content_1add06d1` ON `con_content` (`topicCodeID`);
ALTER TABLE `con_content` ADD CONSTRAINT `con_content_topicCodeID_350ab278_fk_com_code_codeID` FOREIGN KEY (`topicCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `con_chapterDetail_3837e609` ON `con_chapterDetail` (`chapterID`);
ALTER TABLE `con_chapterDetail` ADD CONSTRAINT `con_chapterDetail_chapterID_1d0ca4ad_fk_con_chapter_chapterID` FOREIGN KEY (`chapterID`) REFERENCES `con_chapter` (`chapterID`);
CREATE INDEX `con_chapter_34998857` ON `con_chapter` (`createdBy`);
ALTER TABLE `con_chapter` ADD CONSTRAINT `con_chapter_createdBy_4fac67d0_fk_usr_user_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `con_chapter_63b116d0` ON `con_chapter` (`gradeCodeID`);
ALTER TABLE `con_chapter` ADD CONSTRAINT `con_chapter_gradeCodeID_410f18c7_fk_com_code_codeID` FOREIGN KEY (`gradeCodeID`) REFERENCES `com_code` (`codeID`);
CREATE INDEX `con_chapter_91d33cc8` ON `con_chapter` (`modifiedBy`);
ALTER TABLE `con_chapter` ADD CONSTRAINT `con_chapter_modifiedBy_0c5710f4_fk_usr_user_userID` FOREIGN KEY (`modifiedBy`) REFERENCES `usr_user` (`userID`);
CREATE INDEX `con_chapter_4141fbda` ON `con_chapter` (`subjectCodeID`);
ALTER TABLE `con_chapter` ADD CONSTRAINT `con_chapter_subjectCodeID_74e7544f_fk_com_code_codeID` FOREIGN KEY (`subjectCodeID`) REFERENCES `com_code` (`codeID`);


ALTER TABLE `usr_userContent` ADD CONSTRAINT `usr_userContent_contentID_97d01a29_fk_con_content_contentID` FOREIGN KEY (`contentID`) REFERENCES `con_content` (`contentID`);

COMMIT;

-- events 0001

BEGIN;
--
-- Create model districtBlockMapping
--
CREATE TABLE `evt_districtBlockMapping` (`districtBlockMappingID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `blockCodeID` integer NOT NULL, `districtCodeID` integer NOT NULL, `stateCodeID` integer NOT NULL);
--
-- Create model event
--
CREATE TABLE `evt_event` (`eventID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `createdOn` datetime NOT NULL, `categoryCodeID` integer NOT NULL, `createdBy` integer NOT NULL);
--
-- Create model eventDetail
--
CREATE TABLE `evt_eventDetail` (`eventDetailID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `date` datetime NOT NULL, `engLocation` varchar(255) NULL, `marLocation` varchar(255) NULL, `engTrainer` varchar(255) NULL, `marTrainer` varchar(255) NULL, `createdOn` datetime NOT NULL, `modifiedOn` datetime NOT NULL, `blockCodeID` integer NOT NULL, `createdBy` integer NOT NULL, `districtCodeID` integer NOT NULL, `eventID` integer NOT NULL, `modifiedBy` integer NOT NULL, `stateCodeID` integer NOT NULL, `statusCodeID` integer NOT NULL);
--
-- Create model eventInfo
--
CREATE TABLE `evt_eventInfo` (`eventInfoID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `eventTitle` varchar(2555) NULL, `eventDescription` longtext NULL, `appLanguageCodeID` integer NOT NULL, `eventID` integer NOT NULL);
--
-- Create model usersEvent
--
CREATE TABLE `evt_usersEvent` (`userEventID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY, `createdOn` datetime NOT NULL, `eventDetailID` integer NOT NULL, `userID` integer NOT NULL);
--
-- Alter unique_together for districtblockmapping (1 constraint(s))
--
ALTER TABLE `evt_districtBlockMapping` ADD CONSTRAINT `evt_districtBlockMapping_districtCodeID_5b5d2add_uniq` UNIQUE (`districtCodeID`, `blockCodeID`, `stateCodeID`);
ALTER TABLE `evt_districtBlockMapping` ADD CONSTRAINT `evt_districtBlockMapping_blockCodeID_42da7e59_fk_com_code_codeID` FOREIGN KEY (`blockCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `evt_districtBlockMapping` ADD CONSTRAINT `evt_districtBlockMapp_districtCodeID_1c8dfa10_fk_com_code_codeID` FOREIGN KEY (`districtCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `evt_districtBlockMapping` ADD CONSTRAINT `evt_districtBlockMapping_stateCodeID_143b1eab_fk_com_code_codeID` FOREIGN KEY (`stateCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `evt_event` ADD CONSTRAINT `evt_event_categoryCodeID_faec4658_fk_com_code_codeID` FOREIGN KEY (`categoryCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `evt_event` ADD CONSTRAINT `evt_event_createdBy_44db1619_fk_usr_user_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `evt_eventDetail` ADD CONSTRAINT `evt_eventDetail_blockCodeID_d8159648_fk_com_code_codeID` FOREIGN KEY (`blockCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `evt_eventDetail` ADD CONSTRAINT `evt_eventDetail_createdBy_83916db0_fk_usr_user_userID` FOREIGN KEY (`createdBy`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `evt_eventDetail` ADD CONSTRAINT `evt_eventDetail_districtCodeID_ffaa9931_fk_com_code_codeID` FOREIGN KEY (`districtCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `evt_eventDetail` ADD CONSTRAINT `evt_eventDetail_eventID_03186ba5_fk_evt_event_eventID` FOREIGN KEY (`eventID`) REFERENCES `evt_event` (`eventID`);
ALTER TABLE `evt_eventDetail` ADD CONSTRAINT `evt_eventDetail_modifiedBy_2c1e9336_fk_usr_user_userID` FOREIGN KEY (`modifiedBy`) REFERENCES `usr_user` (`userID`);
ALTER TABLE `evt_eventDetail` ADD CONSTRAINT `evt_eventDetail_stateCodeID_840fcc4f_fk_com_code_codeID` FOREIGN KEY (`stateCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `evt_eventDetail` ADD CONSTRAINT `evt_eventDetail_statusCodeID_ec443d51_fk_com_code_codeID` FOREIGN KEY (`statusCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `evt_eventInfo` ADD CONSTRAINT `evt_eventInfo_appLanguageCodeID_148a2ef0_fk_com_code_codeID` FOREIGN KEY (`appLanguageCodeID`) REFERENCES `com_code` (`codeID`);
ALTER TABLE `evt_eventInfo` ADD CONSTRAINT `evt_eventInfo_eventID_b72119d5_fk_evt_event_eventID` FOREIGN KEY (`eventID`) REFERENCES `evt_event` (`eventID`);
ALTER TABLE `evt_usersEvent` ADD CONSTRAINT `evt_user_eventDetailID_8b6a0ea7_fk_evt_eventDetail_eventDetailID` FOREIGN KEY (`eventDetailID`) REFERENCES `evt_eventDetail` (`eventDetailID`);
ALTER TABLE `evt_usersEvent` ADD CONSTRAINT `evt_usersEvent_userID_61fc42fa_fk_usr_user_userID` FOREIGN KEY (`userID`) REFERENCES `usr_user` (`userID`);

COMMIT;

