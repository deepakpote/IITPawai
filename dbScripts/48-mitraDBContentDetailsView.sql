-- create content view.

CREATE VIEW vw_con_contentDetail AS
SELECT CC.contentID,
	   CC.requirement,
       CC.fileName,
       CC.objectives,
       CC.createdOn,
       CC.modifiedOn,
       CC.contentTypeCodeID,
       CC.createdBy,
       CC.fileTypeCodeID,
       CC.languageCodeID,
       CC.modifiedBy,
       CC.subjectCodeID,
       CC.topicCodeID,
       CC.statusCodeID,
       CC.chapterID,
       CD.contentDetailID,
       CD.contentTitle,
       CD.instruction,
       CD.author,
       CD.appLanguageCodeID
	   FROM con_content CC INNER JOIN con_contentDetail CD ON CC.contentID = CD.contentID;
       
-- update into com_configuration
update com_configuration set `value` = '48-mitraDBContentDetailsView' where `key` = 'dbScriptExecutedUntil';