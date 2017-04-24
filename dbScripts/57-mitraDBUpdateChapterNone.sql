INSERT INTO con_chapter(createdOn, modifiedOn, createdBy, gradeCodeID, modifiedBy, subjectCodeID) 
VALUES (now(), now(), 1, 104100, 1, 103100);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('None', 'None',LAST_INSERT_ID());

INSERT INTO con_chapter(createdOn, modifiedOn, createdBy, gradeCodeID, modifiedBy, subjectCodeID) 
VALUES (now(), now(), 1, 104100, 1, 103101);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('None', 'None',LAST_INSERT_ID());

INSERT INTO con_chapter(createdOn, modifiedOn, createdBy, gradeCodeID, modifiedBy, subjectCodeID) 
VALUES (now(), now(), 1, 104100, 1, 103102);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('None', 'None',LAST_INSERT_ID());


INSERT INTO con_chapter(createdOn, modifiedOn, createdBy, gradeCodeID, modifiedBy, subjectCodeID) 
VALUES (now(), now(), 1, 104101, 1, 103100);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('None', 'None',LAST_INSERT_ID());

INSERT INTO con_chapter(createdOn, modifiedOn, createdBy, gradeCodeID, modifiedBy, subjectCodeID) 
VALUES (now(), now(), 1, 104101, 1, 103101);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('None', 'None',LAST_INSERT_ID());

INSERT INTO con_chapter(createdOn, modifiedOn, createdBy, gradeCodeID, modifiedBy, subjectCodeID) 
VALUES (now(), now(), 1, 104101, 1, 103102);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('None', 'None',LAST_INSERT_ID());



INSERT INTO con_chapter(createdOn, modifiedOn, createdBy, gradeCodeID, modifiedBy, subjectCodeID) 
VALUES (now(), now(), 1, 104102, 1, 103100);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('None', 'None',LAST_INSERT_ID());

INSERT INTO con_chapter(createdOn, modifiedOn, createdBy, gradeCodeID, modifiedBy, subjectCodeID) 
VALUES (now(), now(), 1, 104102, 1, 103101);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('None', 'None',LAST_INSERT_ID());

INSERT INTO con_chapter(createdOn, modifiedOn, createdBy, gradeCodeID, modifiedBy, subjectCodeID) 
VALUES (now(), now(), 1, 104102, 1, 103102);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('None', 'None',LAST_INSERT_ID());



INSERT INTO con_chapter(createdOn, modifiedOn, createdBy, gradeCodeID, modifiedBy, subjectCodeID) 
VALUES (now(), now(), 1, 104103, 1, 103100);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('None', 'None',LAST_INSERT_ID());

INSERT INTO con_chapter(createdOn, modifiedOn, createdBy, gradeCodeID, modifiedBy, subjectCodeID) 
VALUES (now(), now(), 1, 104103, 1, 103101);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('None', 'None',LAST_INSERT_ID());

INSERT INTO con_chapter(createdOn, modifiedOn, createdBy, gradeCodeID, modifiedBy, subjectCodeID) 
VALUES (now(), now(), 1, 104103, 1, 103102);
INSERT INTO con_chapterDetail(chapterEng,chapterMar,chapterID)VALUES ('None', 'None',LAST_INSERT_ID());

-- update into com_configuration
update com_configuration set `value` = '57-mitraDBUpdateChapterNone' where `key` = 'dbScriptExecutedUntil';
